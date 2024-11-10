package eu.tutorials.chatroomapp.screen

import AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val statusBarColor = Color(0xFF003366)
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
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
                    containerColor = Color.Black
                ),
                title = {
                    Text(
                        text = "Home",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.DefaultScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { inclusive = true }
                            popUpTo(0) // Ensure to pop to the root
                        }
                    }) {
                        Icon(
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF003366)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF180b42))
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp)) // White container with rounded corners
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.jlogo), // Add logo if used in login/signup
                        contentDescription = "APP_LOGO",
                        modifier = Modifier.size(80.dp).padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = { navController.navigate(Screen.RoomAllotment.route) },
                        enabled = roomNumber.isNullOrEmpty(), // Enable if roomNumber is empty
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5998)), // Blue button color
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(text = "Allotment", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Navigate to rules screen */ },
                        enabled = true, // Rules button can always be enabled
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5998)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(text = "Rules", color = Color.White)
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Button(
                        onClick = { navController.navigate(Screen.HomeScreen.route) },
                        enabled = !roomNumber.isNullOrEmpty(), // Enable if roomNumber is not empty
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5998)), // Dark red color for Problems button
                        shape = RoundedCornerShape(24.dp)

                    ) {
                        Text(text = "Problems")
                    }
                }
            }
        }
    }
}
