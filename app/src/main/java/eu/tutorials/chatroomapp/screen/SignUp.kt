import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen
import eu.tutorials.chatroomapp.data.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var expandedHostel by remember { mutableStateOf(false) }
    var selectedHostel by remember { mutableStateOf("") }
    val hostelOptions = listOf("LBS", "BVB", "GANGA", "ALP", "AKM")
    var expandedOccupancy by remember { mutableStateOf(false) }
    var selectedOccupancy by remember { mutableStateOf("") }
    val occupancyOptions = listOf("single", "double", "triple")
    var expandedPriority by remember { mutableStateOf(false) }
    var selectedpriority by remember { mutableStateOf("") }
    val priorityOptions = (1..100).map { it.toString() }
    val authResult by authViewModel.authResult.observeAsState()

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
        // Add scrollable content
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make content scrollable
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

            // First Name Field
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name", color = emailPlaceholderColor) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = emailPlaceholderColor) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = passwordPlaceholderColor) },
                leadingIcon = {
                    val passwordAnimationComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.passwordanime4))
                    Box(
                        modifier = Modifier.size(34.dp)
                    ) {
                        LottieAnimation(
                            composition = passwordAnimationComposition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true
            )

            // Hostel Dropdown
            DropdownField(
                label = "Select Hostel",
                options = hostelOptions,
                expanded = expandedHostel,
                selectedOption = selectedHostel,
                onOptionSelected = { selectedHostel = it; expandedHostel = false },
                onExpandedChange = { expandedHostel = !expandedHostel }
            )

            // Occupancy Dropdown
            DropdownField(
                label = "Select Occupancy",
                options = occupancyOptions,
                expanded = expandedOccupancy,
                selectedOption = selectedOccupancy,
                onOptionSelected = { selectedOccupancy = it; expandedOccupancy = false },
                onExpandedChange = { expandedOccupancy = !expandedOccupancy }
            )

            // Priority Dropdown
            DropdownField(
                label = "Select Priority No.",
                options = priorityOptions,
                expanded = expandedPriority,
                selectedOption = selectedpriority,
                onOptionSelected = { selectedpriority = it; expandedPriority = false },
                onExpandedChange = { expandedPriority = !expandedPriority }
            )

            // Sign Up Button
            Button(
                onClick = {
                    authViewModel.signUp(email, password, firstName, selectedHostel, selectedpriority, selectedOccupancy)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("SIGN UP", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate to Login
            Text(
                "Already have an account? Sign in.",
                modifier = Modifier
                    .clickable { onNavigateToLogin() }
                    .padding(8.dp),
                color = Color(0xFF1E88E5),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    expanded: Boolean,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onExpandedChange: () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}
