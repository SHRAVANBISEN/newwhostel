package eu.tutorials.mywishlistapp

import AuthViewModel
import WishViewModel
import android.annotation.SuppressLint
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.chatroomapp.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditDetailView(
    id: Long,
    navController: NavController,
    viewModel: WishViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val systemUiController = rememberSystemUiController()//...
    val statusBarColor = Color.Black //..
    //THIS LAUNCHEDEFFECT FUNCTION HELPS YOU TO GET THE STATUS
    // BAR TRANSPARENT ONCE YOU OPEN THE APP ,AUTOMATICALLY WITHOUT ANY DELAY
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }
    // State to hold the room number
    var roomNumber by remember { mutableStateOf<Int?>(null) }

    // Fetch the room number
    LaunchedEffect(Unit) {
        getRoomNumberFromAuth { number ->
            roomNumber = number
        }
    }

    val wishTitleState by viewModel.wishTitleState.collectAsState()
    val wishDescriptionState by viewModel.wishDescriptionState.collectAsState()

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(modifier = Modifier.fillMaxWidth(),
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black)
                    ),
                    title = {
                        Text(
                            text = "Add Problems",
                            color = colorResource(id = eu.tutorials.chatroomapp.R.color.white),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screen.HomeScreen.route) {
                                popUpTo(Screen.HomeScreen.route) { inclusive = true }
                            }
                        }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }

                )
            },
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = wishTitleState,
                    onValueChange = { viewModel.onWishTitleChanged(it) },
                    label = {
                        androidx.compose.material3.Text(
                            text = "Text", color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 22.dp, start = 5.dp,)
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                        .background(Color.Transparent),
                    shape = RoundedCornerShape(20.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        // using predefined Color
                        textColor = Color.Black,
                        // using our own colors in Res.Values.Color
                        backgroundColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black),
                        focusedBorderColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black),
                        unfocusedBorderColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black),
                        cursorColor = colorResource(id = eu.tutorials.chatroomapp.R.color.black),
                    ),
                )

                OutlinedTextField(
                    value = wishDescriptionState,
                    onValueChange = { viewModel.onWishDescriptionChanged(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )

                Button(
                    onClick = {
                        if (wishTitleState.isNotBlank() && wishDescriptionState.isNotBlank()) {
                            val userId = getUserIdFromAuth()
                            val useridd = getUserIdFromAuthh()
                            roomNumber?.let { roomnum ->
                                viewModel.addWish(userId, useridd, roomnum)
                                scope.launch {
                                    navController.navigateUp()
                                    Toast.makeText(
                                        navController.context,
                                        "Swipe left to delete",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } ?: run {
                                Toast.makeText(
                                    navController.context,
                                    "Room number is not available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                navController.context,
                                "Please enter title and description",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Add Wish")
                }
            }
        }
    }
}
//THESE TWO FUNCTIONS ARE GAME CHANGER
fun getUserIdFromAuth(): String {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser?.email ?: "user123@gmail.com" // Dummy user ID for testing
}// Dummy function, replace with actual logic to fetch user ID from authentication system

fun getUserIdFromAuthh(): String {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser?.uid ?: "user123" // Dummy user ID for testing
}




fun getRoomNumberFromAuth(callback: (Int?) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.email ?: run {
        callback(null)
        return
    }

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(userId)
        .get()
        .addOnSuccessListener { documentSnapshot ->
            val roomNumberString = documentSnapshot.getString("roomNumber")
            val roomNumber = roomNumberString?.toIntOrNull()
            callback(roomNumber)
        }
        .addOnFailureListener {
            callback(null)
        }
}

