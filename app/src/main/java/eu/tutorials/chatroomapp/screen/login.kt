package eu.tutorials.chatroomapp.screen

import AuthViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.data.Result

@OptIn(ExperimentalMaterial3Api::class)
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
            }
        }
    }

    // Animation setup
    val infiniteTransition = rememberInfiniteTransition()
    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val emailPlaceholderColor by animateColorAsState(
        targetValue = if (email.isEmpty()) Color.Gray else Color(0xFF1E88E5)
    )

    val passwordPlaceholderColor by animateColorAsState(
        targetValue = if (password.isEmpty()) Color.Gray else Color(0xFF1E88E5)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF003366), Color(0xFF1E88E5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Logo
            val logoComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anime3))
            LottieAnimation(
                composition = logoComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(120.dp)
                    .scale(animatedScale)
                    .padding(bottom = 16.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = emailPlaceholderColor) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // Password TextField with Lottie animation on the password icon
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = passwordPlaceholderColor) }, // Apply password color here
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true,
                leadingIcon = {
                    val passwordAnimationComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.passwordanime4))
                    Box(
                        modifier = Modifier.size(34.dp) // Increase size here for the icon
                    ) {
                        LottieAnimation(
                            composition = passwordAnimationComposition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // Login Button
            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("LOGIN", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign up text
            Text(
                "Don't have an account? Sign up.",
                modifier = Modifier
                    .clickable { onNavigateToSignUp() }
                    .padding(8.dp),
                color = Color(0xFF1E88E5),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            )
        }

        // Loading Animation
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anime2))
                LottieAnimation(
                    composition = loadingComposition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }
}
