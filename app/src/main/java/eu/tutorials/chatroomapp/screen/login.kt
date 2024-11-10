package eu.tutorials.chatroomapp.screen

import AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.data.Result

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit,
    onPrincipalSignIn: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by authViewModel.isLoading.observeAsState(initial = false)
    val result by authViewModel.authResult.observeAsState()

    // Handle navigation on successful login
    LaunchedEffect(result) {
        result?.let {
            if (it is Result.Success) {
                if (authViewModel.isPrincipalUser(email, password)) {
                    onPrincipalSignIn()
                } else {
                    onSignInSuccess()
                }
                email = ""
                password = ""
                authViewModel.clearAuthResult()
            } else if (it is Result.Error) {
                // Handle error (e.g., show a toast message)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180b42)), // Optional: set a background color
        contentAlignment = Alignment.Center
    ) {
        // Main UI for Login Screen
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(200.dp))



            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(24.dp)) // White background with rounded corners
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.luser), // Replace with actual logo resource ID
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email Icon")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Password Icon")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                Button(
                    onClick = {
                        authViewModel.login(email, password)
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)), // Button color
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Text("LOGIN", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign up text
                Text(
                    "Don't have an account? Sign up.",
                    modifier = Modifier.clickable { onNavigateToSignUp() },
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, // Make text bold
                    textDecoration = TextDecoration.Underline
                )

            }
        }

        // Full-screen Lottie animation when loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)) // Optional: semi-transparent background
                    .clickable(enabled = false) {}, // Block clicks while loading
                contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anime2))
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp) // Adjust size as needed
                )
            }
        }
    }
}
