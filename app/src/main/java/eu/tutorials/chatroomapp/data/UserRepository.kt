package eu.tutorials.chatroomapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(email: String, password: String, firstName: String, hostelName: String, roomNumber: String): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, email, hostelName, roomNumber)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getAllWishes(): List<Wish> {
        return try {
            val snapshot = firestore.collection("wishes").get().await()
            snapshot.documents.mapNotNull { it.toObject(Wish::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWishesByHostel(hostelName: String): List<Wish> {
        return try {
            // First, get all users who belong to the specified hostel
            val userSnapshots = firestore.collection("users")
                .whereEqualTo("hostelName", hostelName)
                .get()
                .await()

            // Extract user emails
            val userEmails = userSnapshots.documents.mapNotNull { it.getString("email") }

            if (userEmails.isNotEmpty()) {
                // Get wishes added by users who belong to the specified hostel
                val wishSnapshots = firestore.collection("wishes")
                    .whereIn("userEmail", userEmails)
                    .get()
                    .await()

                wishSnapshots.documents.mapNotNull { it.toObject(Wish::class.java) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun getCurrentUser(): Result<User> = try {
        val email = auth.currentUser?.email
        if (email != null) {
            val userDocument = firestore.collection("users").document(email).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}
