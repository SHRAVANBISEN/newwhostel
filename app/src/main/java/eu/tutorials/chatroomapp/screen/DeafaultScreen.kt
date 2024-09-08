package eu.tutorials.chatroomapp.screen

import AuthViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.tutorials.chatroomapp.Screen


@Composable
fun DefaultView(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val isUserLoggedIn by authViewModel.isUserLoggedIn.observeAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (isUserLoggedIn) {
                    Toast.makeText(context, "Already Logged In", Toast.LENGTH_SHORT).show()
                    // Navigate to HomeScreen or PrincipalScreen based on the user
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.DefaultScreen.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.LoginScreen.route)
                }
            },
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.SignupScreen.route)
            },
            modifier = Modifier.width(200.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
