package eu.tutorials.chatroomapp.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Wish(
    @DocumentId
    var id: String = "", // Firestore document ID
    var userId: String = "", // ID of the user who owns this wish (assuming Firebase Authentication UID)
    var title: String = "",
    var description: String = "",
    var hostelName : String = "",
    var roomnum : Int = 0,
    var useridd  :String = "",
    @Exclude // Exclude from Firestore serialization/deserialization
    var isNew: Boolean = false // Flag to indicate if this is a new wish (local UI state)
)
