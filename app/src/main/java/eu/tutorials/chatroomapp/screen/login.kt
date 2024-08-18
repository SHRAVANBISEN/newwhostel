package eu.tutorials.chatroomapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
//THIS LAUNCHED EFFECT IS WORKING ON PROPER NAVIGATIONS BETWEEN THE SCREENS ON SUCCEFULL LOGIN AND LOGOUT
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
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Login")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Don't have an account? Sign up.",
            modifier = Modifier.clickable { onNavigateToSignUp() }
        )
    }
}

