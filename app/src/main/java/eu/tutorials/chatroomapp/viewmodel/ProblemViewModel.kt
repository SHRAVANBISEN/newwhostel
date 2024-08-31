
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.chatroomapp.data.UserRepository
import eu.tutorials.chatroomapp.data.Wish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await




class WishViewModel(private val repository: UserRepository) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _wishes = MutableStateFlow<List<Wish>>(emptyList())
    val wishes: StateFlow<List<Wish>> get() = _wishes

    private val _wishTitleState = MutableStateFlow("")
    val wishTitleState: StateFlow<String> get() = _wishTitleState

    private val _wishDescriptionState = MutableStateFlow("")
    val wishDescriptionState: StateFlow<String> get() = _wishDescriptionState

    constructor() : this(UserRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())) {
        // Initialize repository with default Firebase instances if needed
    }

    fun loadWishes() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("wishes")
            .whereEqualTo("useridd", userId)
            .get()
            .addOnSuccessListener { documents ->
                val wishList = documents.map { doc -> doc.toObject(Wish::class.java) }
                _wishes.value = wishList
            }
    }

    fun loadWishesByHostel(hostelName: String) {
        viewModelScope.launch {
            val userDocs = db.collection("users")
                .whereEqualTo("hostelName", hostelName)
                .get()
                .await()

            val userIds = userDocs.documents.mapNotNull { it.id }

            if (userIds.isNotEmpty()) {
                val wishDocs = db.collection("wishes")
                    .whereIn("userId", userIds)
                    .get()
                    .await()

                val wishList = wishDocs.documents.mapNotNull { it.toObject(Wish::class.java) }
                _wishes.value = wishList
            } else {
                _wishes.value = emptyList()
            }
        }
    }
//BY ADDING TWO TYPES OF IDS IN ADDWISH FUNC I WAS ABLE TO DISPLAY PROBLEMS IN PARTICULAR HOSTEL PROBLEM SCREEN AS WELL AS ON USER PROBLEM SCREEN ONLY THE
// PROBLEMS ADDED BY THAT PARTICULAR USER WILL BE DISPLAYED
    fun addWish(userId: String , useridd : String , roomnum  :Int) {
        val wish = Wish(
            title = _wishTitleState.value,
            description = _wishDescriptionState.value,
            userId = userId,
            useridd =useridd,
            roomnum = roomnum
        )
        db.collection("wishes")
            .add(wish)
            .addOnSuccessListener {
                // Clear the state variables after successful addition
                _wishTitleState.value = ""
                _wishDescriptionState.value = ""
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun deleteWish(wish: Wish) {
        db.collection("wishes").document(wish.id)
            .delete()
            .addOnSuccessListener {
                loadWishes()
            }
    }

    fun onWishTitleChanged(newTitle: String) {
        _wishTitleState.value = newTitle
    }

    fun onWishDescriptionChanged(newDescription: String) {
        _wishDescriptionState.value = newDescription
    }
}
