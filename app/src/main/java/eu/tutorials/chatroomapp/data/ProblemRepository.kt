package eu.tutorials.chatroomapp.data




import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WishRepository {

    private val wishesCollection: CollectionReference =
        FirebaseFirestore.getInstance().collection("wishes")

    suspend fun addAWish(wish: Wish) {
        wishesCollection.add(wish).await()
    }

    suspend fun updateAWish(wish: Wish) {
        wishesCollection.document(wish.id.toString()).set(wish).await()
    }

    suspend fun deleteAWish(wish: Wish) {
        wishesCollection.document(wish.id.toString()).delete().await()
    }

    suspend fun getAWishById(id: Long): Wish {
        val documentSnapshot = wishesCollection.document(id.toString()).get().await()
        return documentSnapshot.toObject(Wish::class.java)!!
    }

    fun getWishes(): CollectionReference {
        return wishesCollection
    }
}
