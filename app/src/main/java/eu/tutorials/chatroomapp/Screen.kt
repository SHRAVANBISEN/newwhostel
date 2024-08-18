package eu.tutorials.chatroomapp

sealed class Screen(val route: String) {
    object LoginScreen : Screen("loginscreen")
    object SignupScreen : Screen("signupscreen")
    object HomeScreen : Screen("home_screen")
    object AddScreen : Screen("add_screen")
    object DefaultScreen : Screen("Default_screen")
    object PrincipleScreen : Screen("PrincipleScreen")
    object BVBHOSTEL : Screen("bvbhostel")
    object LBSHOSTEL : Screen("lbshostel")
    object ALPHOSTEL : Screen("alphostel")
    object GANGA : Screen("gangahostel")
    object AKKAM : Screen("AKKAMHOSTEL")



}
