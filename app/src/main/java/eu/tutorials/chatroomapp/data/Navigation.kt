package eu.tutorials.chatroomapp.data

import AuthViewModel
import RoomDet
import SignupScreen
import WishViewModel
import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.tutorials.chatroomapp.Screen
import eu.tutorials.chatroomapp.screen.*
import eu.tutorials.mywishlistapp.AddEditDetailView


@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    wishViewModel: WishViewModel
) {// NavigationGraph.kt
    val context = navController.context
    fun isPrincipalUserOnLaunch(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isPrincipalUser", false)
    }
    val isPrincipalUser = remember { isPrincipalUserOnLaunch(context) }

    val isUserLoggedIn: Boolean by authViewModel.isUserLoggedIn.observeAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = when {
            isUserLoggedIn && isPrincipalUser -> Screen.PrincipleScreen.route
            isUserLoggedIn -> Screen.StudentsScreen.route
            else -> Screen.DefaultScreen.route
        }
    ) {
        composable(Screen.SignupScreen.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route)
                {
                    popUpTo(Screen.SignupScreen.route) { inclusive = true }
                }}
            )
        }
        composable(Screen.DefaultScreen.route) {
            DefaultView(navController = navController ,authViewModel = authViewModel)
        }
        composable(Screen.RoomAllotment.route) {
            RoomAllotment(navController = navController ,authViewModel = authViewModel)
        }
        composable(Screen.StudentsScreen.route) {
            StudentScreen(navController = navController , authViewModel = authViewModel)
        }


        composable(Screen.RoomDet.route) {
            RoomDet(navController = navController )
        }
        composable(Screen.PrincipleScreen.route) {
            PrincipleScreen( navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = {
                    navController.navigate(Screen.StudentsScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                },
                onPrincipalSignIn = {
                    navController.navigate(Screen.PrincipleScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.HomeScreen.route) {
            HomeView(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screen.BVBHOSTEL.route) {
            BVBVIEW(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screen.AKKAM.route) {
            AKKVIEW(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screen.LBSHOSTEL.route) {
            LBSVIEW(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screen.ALPHOSTEL.route) {
            ALPVIEW(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screen.GANGA.route) {
            GANGAVIEW(
                navController = navController,
                viewModel = wishViewModel,
                authViewModel = authViewModel
            )
        }
        composable("${Screen.AddScreen.route}/{wishId}") { backStackEntry ->
            val wishIdString = backStackEntry.arguments?.getString("wishId") ?: "0"
            val wishId = wishIdString.toLongOrNull() ?: 0L
            AddEditDetailView(
                id = wishId,
                viewModel = wishViewModel,
                navController = navController
            )
        }
    }

    if (isUserLoggedIn && navController.currentBackStackEntry?.destination?.route == Screen.HomeScreen.route) {
        BackHandler {
            (navController.context as? Activity)?.finish()
        }
    }
}
