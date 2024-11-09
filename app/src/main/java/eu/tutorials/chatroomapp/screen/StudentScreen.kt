package eu.tutorials.chatroomapp.screen

import AuthViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(navController: NavController ,authViewModel: AuthViewModel) {
    var roomNumber by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }
    // Function to fetch user data
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.email ?: return@LaunchedEffect
        FirebaseFirestore.getInstance().collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    roomNumber = document.getString("roomNumber")
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.black)
                ),
                title = {
                    androidx.compose.material.Text(
                        text = "Home",
                        color = colorResource(id = R.color.white)
                    )
                },
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.DefaultScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { inclusive = true }
                            popUpTo(0) // Ensure to pop to the root
                        }
                    }) {
                        androidx.compose.material.Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
         Text(text = "Loading....")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate(Screen.RoomAllotment.route) },
                    enabled = roomNumber.isNullOrEmpty() // Enable if roomNumber is empty
                ) {
                    Text(text = "Allotment")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { /* Navigate to rules screen */ },
                    enabled = true // Rules button can always be enabled
                ) {
                    Text(text = "Rules")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { navController.navigate(Screen.HomeScreen.route) },
                    enabled = !roomNumber.isNullOrEmpty() // Enable if roomNumber is not empty
                ) {
                    Text(text = "Problems")
                }
            }
        }
    }
}
