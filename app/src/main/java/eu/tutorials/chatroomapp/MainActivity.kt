package eu.tutorials.chatroomapp

import AuthViewModel
import WishViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.tutorials.chatroomapp.data.NavigationGraph
import eu.tutorials.chatroomapp.data.UserRepository
import eu.tutorials.chatroomapp.screen.LoginScreen
import eu.tutorials.chatroomapp.ui.theme.ChatRoomAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(50)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val wishViewModel: WishViewModel = viewModel()

            ChatRoomAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, authViewModel = authViewModel , wishViewModel = wishViewModel)
                }
            }
        }
    }
}



