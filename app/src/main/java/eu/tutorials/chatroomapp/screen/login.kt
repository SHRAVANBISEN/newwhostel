package eu.tutorials.chatroomapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.data.Result
import eu.tutorials.chatroomapp.viewmodel.AuthViewModel

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
            .background(Color.White) // Optional: set a background color
    ) {
        // Main UI for Login Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Don't have an account? Sign up.",
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }

        // Full-screen Lottie animation when loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)) // Optional: semi-transparent background
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
