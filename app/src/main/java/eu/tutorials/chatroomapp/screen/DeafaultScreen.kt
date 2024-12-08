package eu.tutorials.chatroomapp.screen

import AuthViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen


@Composable
fun DefaultView(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val isUserLoggedIn by authViewModel.isUserLoggedIn.observeAsState(false)
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Background color matching the login screen
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

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
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.nmb)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("LOGIN", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.SignupScreen.route)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.nmb)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Sign Up", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

    }
}
