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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen
import eu.tutorials.chatroomapp.data.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }

    // Dropdown menu variables
    var expandedHostel  by remember { mutableStateOf(false) }
    var selectedHostel by remember { mutableStateOf("") }
    val hostelOptions = listOf("LBS", "BVB", "GANGA" , "ALP" , "AKM")

    // Dropdown menu for occupancy
    var expandedOccupancy  by remember { mutableStateOf(false) }
    var selectedOccupancy by remember { mutableStateOf("") }
    val occupancyOptions = listOf("single", "double", "triple" )

    // Dropdown menu variables for roomnum
    var expandedPriority  by remember { mutableStateOf(false) }
    var selectedpriority by remember { mutableStateOf("") }
    val priorityOptions  =  (1..100).map { it.toString() }

    val authResult by authViewModel.authResult.observeAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180b42)),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.luser), // Placeholder for signup logo
                contentDescription = "Sign-Up Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
            )

            // First Name Field
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
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

            Button(
                onClick = {
                    authViewModel.signUp(email, password, firstName, selectedHostel, selectedpriority ,selectedOccupancy)
                    email = ""
                    password = ""
                    firstName = ""
                    selectedHostel = ""
                    selectedpriority = ""
                    selectedOccupancy= ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("SIGN UP", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Already have an account? Sign in.",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    authViewModel.clearAllAuthStates() // Clear any leftover auth states
                    onNavigateToLogin() }
            )

            authResult?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                when (result) {
                    is Result.Success -> {
                        Text("Registration successful")
                    }
                    is Result.Error -> {
                        Text("Registration failed")
                    }
                }
            }
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
