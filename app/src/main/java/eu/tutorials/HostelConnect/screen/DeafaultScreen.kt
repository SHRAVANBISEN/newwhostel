package eu.tutorials.HostelConnect.screen

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.tutorials.HostelConnect.R
import eu.tutorials.HostelConnect.Screen
import eu.tutorials.HostelConnect.ui.theme.AlegreyaFontFamily
import eu.tutorials.HostelConnect.ui.theme.AlegreyaSansFontFamily
import eu.tutorials.HostelConnect.viewmodel.AuthViewModel


@Composable
fun DefaultView(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val isUserLoggedIn by authViewModel.isUserLoggedIn.observeAsState(false)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg001),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.width(320.dp).height(240.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Hello",
                textAlign = TextAlign.Center,
                fontFamily = AlegreyaSansFontFamily,
                fontSize = 23.sp,
                fontWeight = FontWeight(500),
                color = Color.White
            )
            Text(
                text = "Welcome to HostelProSolve App",
                textAlign = TextAlign.Center,
                fontFamily = AlegreyaSansFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                color = Color.White
            )

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
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    //containerColor = Color(0xFF7C9A92  for button color
                ),
                modifier = Modifier.width(350.dp).height(52.dp)
            ) {
                Text("Sign In With Email",
                    fontSize = 22.sp,
                    fontFamily = AlegreyaSansFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color.White
                    )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.SignupScreen.route)
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    //containerColor = Color(0xFF7C9A92  for button color
                ),
                modifier = Modifier.
                height(52.dp).width(350.dp)
            ) {
                Text("Sign Up",
                    fontSize = 22.sp,
                    fontFamily = AlegreyaSansFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

        }

    }
}

