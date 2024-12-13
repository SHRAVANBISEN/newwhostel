package eu.tutorials.chatroomapp.screen

import AuthViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun RoomAllotment(navController: NavController, authViewModel: AuthViewModel) {
    // State to hold the room occupancy data and user-specific information
    var roomOccupancy by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var userOccupancy by remember { mutableStateOf("") }
    var priorityNumber by remember { mutableStateOf(1) }
    var userPriorityNum by remember { mutableStateOf(0) }

    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val privilegedUserId = "shravanbisen@gmail.com"
    val currentUserId = FirebaseAuth.getInstance().currentUser?.email
    val isPrivilegedUser = currentUserId == privilegedUserId

    // Fetch room occupancy data and user occupancy level
    LaunchedEffect(Unit) {
        roomOccupancy = fetchRoomOccupancy()
        userOccupancy = fetchUserOccupancy()
        userPriorityNum = fetchUserPriorityNum()
        priorityNumber = fetchPriorityNumber()
    }

    LaunchedEffect(Unit) {
        // Listen for real-time updates on the priority number document
        db.collection("appData").document("priorityData")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Toast.makeText(context, "Failed to fetch priority number", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    priorityNumber = snapshot.getLong("priorityNumber")?.toInt() ?: 1
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Display the priority number with a refresh button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Priority Number: $priorityNumber", style = MaterialTheme.typography.h6 , color = colorResource(
                id = R.color.black
            ))
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                coroutineScope.launch {
                    priorityNumber = fetchPriorityNumber()
                }
            } , colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))) {
                Text("Refresh")
            }
        }

        // Show "Next" button for privileged user
        if (isPrivilegedUser) {
            Row (modifier = Modifier.fillMaxWidth()){
                Button(onClick = {  decrementPriorityNumber()
                    Toast.makeText(context, "Priority number decremented ", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Previous")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                incrementPriorityNumber()
                Toast.makeText(context, "Priority number incremented", Toast.LENGTH_SHORT).show()
            }) {
                Text("Next")
            }
        }

        // Occupancy sections
        Spacer(modifier = Modifier.height(16.dp))
        Text("Single Occupancy", fontSize = 20.sp , color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        RoomGrid(
            roomNumbers = listOf(1, 2, 3, 22, 33, 44, 55, 66, 77),
            navController = navController,
            roomOccupancy = roomOccupancy,
            occupancyLimit = 1,
            userOccupancy = userOccupancy,
            userPriorityNum = userPriorityNum,
            priorityNumber = priorityNumber
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Double Occupancy" , fontSize = 20.sp , color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))


        RoomGrid(
            roomNumbers = listOf(4, 5, 6, 7, 56, 57, 58, 59, 60),
            navController = navController,
            roomOccupancy = roomOccupancy,
            occupancyLimit = 2,
            userOccupancy = userOccupancy,
            userPriorityNum = userPriorityNum,
            priorityNumber = priorityNumber
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Triple Occupancy", fontSize = 20.sp , color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        RoomGrid(
            roomNumbers = listOf(8, 9, 10, 11, 12, 67, 68, 69, 70),
            navController = navController,
            roomOccupancy = roomOccupancy,
            occupancyLimit = 3,
            userOccupancy = userOccupancy,
            userPriorityNum = userPriorityNum,
            priorityNumber = priorityNumber
        )
    }
}

@Composable
fun RoomGrid(
    roomNumbers: List<Int>,
    navController: NavController,
    roomOccupancy: Map<Int, Int>,
    occupancyLimit: Int,
    userOccupancy: String,
    userPriorityNum: Int,
    priorityNumber: Int
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf<Int?>(null) }
    var existingUserFirstName by remember { mutableStateOf<String?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(roomNumbers) { roomNumber ->
            val currentOccupancy = roomOccupancy[roomNumber] ?: 0
            val isRoomAvailable = currentOccupancy < occupancyLimit

            // Determine if button should be enabled based on conditions
            val isOccupancyMatched = when (userOccupancy) {
                "single" -> occupancyLimit == 1
                "double" -> occupancyLimit == 2
                "triple" -> occupancyLimit == 3
                else -> false
            }
            val isPriorityMatched = userPriorityNum == priorityNumber
            val isButtonEnabled = isRoomAvailable && isOccupancyMatched && isPriorityMatched

            Button(
                onClick = {
                    if (isButtonEnabled) {
                        selectedRoom = roomNumber
                        showDialog = true
                        checkExistingUser(roomNumber) { firstName ->
                            existingUserFirstName = firstName
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "You cannot select a room until your priority number matches.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = colorResource(id = R.color.nmb)),

                enabled = isButtonEnabled
            ) {
                Text(text = "Room $roomNumber")
            }
        }
    }

    if (showDialog && selectedRoom != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Room Allocation") },
            text = {
                if (existingUserFirstName != null) {
                    Text("Room $selectedRoom is occupied by ${existingUserFirstName}.")
                } else {
                    Text("Room $selectedRoom is available.")
                }
            },
            confirmButton = {
                Button(onClick = {
                    saveRoomForCurrentUser(selectedRoom!!)
                    showDialog = false

                    navController.navigate(Screen.StudentsScreen.route)
                    Toast.makeText(
                        context,
                        "Room alloted",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Function to fetch current occupancy data for each room
suspend fun fetchRoomOccupancy(): Map<Int, Int> {
    val db = FirebaseFirestore.getInstance()
    val rooms = mutableMapOf<Int, Int>()

    try {
        val result = db.collection("users").get().await()
        for (document in result.documents) {
            val roomNumber = document.getString("roomNumber")?.toIntOrNull()
            if (roomNumber != null) {
                rooms[roomNumber] = rooms.getOrDefault(roomNumber, 0) + 1
            }
        }
    } catch (e: Exception) {
        // Handle error
    }

    return rooms
}

// Function to fetch the logged-in user's occupancy level
suspend fun fetchUserOccupancy(): String {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.email ?: return ""

    return try {
        val document = db.collection("users").document(userId).get().await()
        document.getString("occupancy") ?: ""
    } catch (e: Exception) {
        ""
    }
}
suspend fun fetchUserPriorityNum(): Int {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.email ?: return 0

    return try {
        val document = db.collection("users").document(userId).get().await()
        document.getString("prioritynum")?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

// Function to check if a specific room is occupied by a user
fun checkExistingUser(roomNumber: Int, onResult: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("roomNumber", roomNumber.toString())
        .get()
        .addOnSuccessListener { documents ->
            val user = documents.documents.firstOrNull()
            val firstName = user?.getString("firstName")
            onResult(firstName)
        }
        .addOnFailureListener {
            onResult(null)
        }
}

// Function to save room number for the current user
fun saveRoomForCurrentUser(roomNumber: Int) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.email

    if (userId != null) {
        db.collection("users").document(userId)
            .update("roomNumber", roomNumber.toString())
            .addOnSuccessListener {
                // Room number saved successfully
            }
            .addOnFailureListener {
                // Handle the error if the update fails
            }
    }
}


fun incrementPriorityNumber() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("appData").document("priorityData")

    // Make sure the document exists, and if not, create it
    docRef.get()
        .addOnSuccessListener { document ->
            if (!document.exists()) {
                // Create the document with a default priority number if it doesn't exist
                docRef.set(mapOf("priorityNumber" to 1))
                    .addOnCompleteListener {
                        incrementPriorityNumberInDb(docRef)
                    }
            } else {
                incrementPriorityNumberInDb(docRef)
            }
        }
}


suspend fun fetchPriorityNumber(): Int {
    val db = FirebaseFirestore.getInstance()
    return try {
        // Try to fetch the priority number document
        val document = db.collection("appData").document("priorityData").get().await()
        if (document.exists()) {
            document.getLong("priorityNumber")?.toInt() ?: 1
        } else {
            // If the document doesn't exist, create it with a default value
            db.collection("appData").document("priorityData")
                .set(mapOf("priorityNumber" to 1))
                .await()
            1  // Return default value
        }
    } catch (e: Exception) {
        // Handle the error, returning a default value
        1
    }
}

private fun incrementPriorityNumberInDb(docRef: DocumentReference) {
    // Increment the priority number in the document
    docRef.update("priorityNumber", FieldValue.increment(1))
        .addOnSuccessListener {
            // This will automatically trigger real-time updates for all users
        }
        .addOnFailureListener {
            // Handle failure if the increment fails
        }
}


fun decrementPriorityNumber() {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("appData").document("priorityData")

    // Ensure the document exists before attempting to decrement
    docRef.get()
        .addOnSuccessListener { document ->
            if (!document.exists()) {
                // If the document doesn't exist, create it with a default value
                docRef.set(mapOf("priorityNumber" to 1))
                    .addOnCompleteListener {
                        decrementPriorityNumberInDb(docRef)
                    }
            } else {
                decrementPriorityNumberInDb(docRef)
            }
        }
        .addOnFailureListener {
            // Handle errors if the document fetch fails
        }
}

private fun decrementPriorityNumberInDb(docRef: DocumentReference) {
    docRef.update("priorityNumber", FieldValue.increment(-1))
        .addOnSuccessListener {
            // Successfully decremented the priority number
        }
        .addOnFailureListener {
            // Handle errors if the update fails
        }
}