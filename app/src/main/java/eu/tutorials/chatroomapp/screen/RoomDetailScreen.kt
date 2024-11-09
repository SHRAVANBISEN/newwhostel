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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RoomDet( navController: NavHostController) {
    // State to store the fetched room numbers
    val roomNumbers = remember { mutableStateListOf<String>() } // Use String to match roomNumber type
    val dialogOpen = remember { mutableStateOf(false) }
    val db = Firebase.firestore // Firestore instance

    // Load room numbers from Firestore once
    LaunchedEffect(Unit) {
        fetchRoomNumbers(db, roomNumbers)
    }

    // Create a grid with 4 columns to display room buttons
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(roomNumbers) { roomNumber ->
            Button(
                onClick = { dialogOpen.value = true },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = roomNumber) // Display room number on each button
            }
        }
    }

    // Show the dialog when dialogOpen is true
    AccountDialog(dialogOpen = dialogOpen)
}

// Function to fetch room numbers from all documents in the "users" collection
fun fetchRoomNumbers(db: FirebaseFirestore, roomNumbers: MutableList<String>) {
    db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val roomNumber = document.getString("roomNumber") ?: ""
                if (roomNumber.isNotEmpty()) {
                    roomNumbers.add(roomNumber) // Add the room number to the list
                }
            }
        }
        .addOnFailureListener { exception ->
            // Handle any errors (optional)
        }
}

@Composable
fun AccountDialog(dialogOpen: MutableState<Boolean>) {
    if (dialogOpen.value) {
        AlertDialog(
            onDismissRequest = { dialogOpen.value = false },
            confirmButton = {
                TextButton(onClick = { dialogOpen.value = false }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogOpen.value = false }) {
                    Text(text = "Dismiss")
                }
            },
            title = {
                Text(text = "Add Account")
            },
            text = {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = "",
                        onValueChange = { /* Handle email input */ },
                        modifier = Modifier.padding(top = 16.dp),
                        label = { Text(text = "Email") }
                    )
                    TextField(
                        value = "",
                        onValueChange = { /* Handle password input */ },
                        modifier = Modifier.padding(top = 8.dp),
                        label = { Text(text = "Password") }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primarySurface)
                .padding(8.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = Color.White,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetPreview() {
    RoomDet(navController = rememberNavController())
}
