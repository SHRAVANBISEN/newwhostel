package eu.tutorials.chatroomapp.screen

import AuthViewModel
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.Screen
import kotlinx.coroutines.delay

@Composable
fun DefaultView(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val isUserLoggedIn by authViewModel.isUserLoggedIn.observeAsState(false)
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF003366)
    var isScreenVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
        delay(300) // Simulate delay for animation
        isScreenVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF003366), Color(0xFF1E88E5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isScreenVisible,
            enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.8f),
            exit = fadeOut(animationSpec = tween(500)) + scaleOut(targetScale = 0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Welcome Text
                Text(
                    text = "Welcome to HostelConnect!",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )

                // Button Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (isUserLoggedIn) {
                                Toast.makeText(context, "Already Logged In", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(Screen.DefaultScreen.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Screen.LoginScreen.route)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Login", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("LOGIN", color = Color.White, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screen.SignupScreen.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Sign Up", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SIGN UP", color = Color.White, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Footer
                Text(
                    text = "Chat securely and connect seamlessly.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}
