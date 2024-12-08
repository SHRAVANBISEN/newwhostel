import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import eu.tutorials.chatroomapp.R

@Composable
fun RoomDet(navController: NavHostController) {
    val roomNumbers = remember { mutableStateMapOf<String, String>() } // Map of room number to hostel name
    val dialogOpen = remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val selectedUsersDetails = remember { mutableStateListOf<UserDetails>() } // List to hold multiple users' details
    val systemUiController = rememberSystemUiController()
    val statusBarColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black)
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }
    LaunchedEffect(Unit) {
        fetchRoomNumbers(db, roomNumbers)
    }
Box(modifier = Modifier
    .fillMaxSize()
    .background(colorResource(id = R.color.black)) )
{
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(roomNumbers.keys.toList()) { roomNumber ->
            Button(
                onClick = {
                    fetchUsersDetails(db, roomNumber, roomNumbers[roomNumber] ?: "") { usersDetails ->
                        selectedUsersDetails.clear()
                        selectedUsersDetails.addAll(usersDetails)
                        dialogOpen.value = true
                    }
                },
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.nmb))
            ) {
                Text(text = roomNumber)
            }
        }
    } 
}
   

    AccountDialog(dialogOpen = dialogOpen, usersDetails = selectedUsersDetails)
}

fun fetchRoomNumbers(db: FirebaseFirestore, roomNumbers: MutableMap<String, String>) {
    db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val roomNumber = document.getString("roomNumber") ?: ""
                val hostelName = document.getString("hostelName") ?: ""
                if (roomNumber.isNotEmpty() && hostelName.isNotEmpty()) {
                    roomNumbers[roomNumber] = hostelName // Store unique room numbers with hostel names
                }
            }
        }
        .addOnFailureListener { exception ->
            // Handle any errors (optional)
        }
}

// Function to fetch user details for a given room number and hostel name
fun fetchUsersDetails(
    db: FirebaseFirestore,
    roomNumber: String,
    hostelName: String,
    onResult: (List<UserDetails>) -> Unit
) {
    db.collection("users")
        .whereEqualTo("roomNumber", roomNumber)
        .whereEqualTo("hostelName", hostelName)
        .get()
        .addOnSuccessListener { result ->
            val usersDetails = result.documents.mapNotNull { document ->
                UserDetails(
                    firstName = document.getString("firstName") ?: "",
                    roomNumber = document.getString("roomNumber") ?: "",
                    hostelName = document.getString("hostelName") ?: ""
                )
            }
            onResult(usersDetails)
        }
        .addOnFailureListener {
            onResult(emptyList()) // Handle any errors (optional)
        }
}

@Composable
fun AccountDialog(dialogOpen: MutableState<Boolean>, usersDetails: List<UserDetails>) {
    if (dialogOpen.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding for border
        ) {
            AlertDialog(
                onDismissRequest = { dialogOpen.value = false },
                confirmButton = {
                    TextButton(onClick = { dialogOpen.value = false }) {
                        Text("Confirm" , color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dialogOpen.value = false }) {
                        Text(text = "Dismiss" , color = Color.Black)
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Room Details",
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.black),
                        fontSize = 20.sp ,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    if (usersDetails.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            usersDetails.forEach { user ->
                                Text(text = "Name: ${user.firstName}"  , color = colorResource(id = R.color.black))
                                Text(text = "Room Number: ${user.roomNumber}" , color = colorResource(
                                    id = R.color.black
                                ))
                                Text(text = "Hostel Name: ${user.hostelName}" , color = colorResource(
                                    id = R.color.black
                                ))
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        Text(text = "No users found for this room number.")
                    }
                },
                modifier = Modifier
                    .background(Color.Transparent),
                shape = RoundedCornerShape(5.dp),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            )
        }
    }
}
data class UserDetails(
    val firstName: String,
    val roomNumber: String,
    val hostelName: String
)

@Preview(showBackground = true)
@Composable
fun RoomDetPreview() {
    RoomDet(navController = rememberNavController())
}
