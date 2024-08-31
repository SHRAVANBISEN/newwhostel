package eu.tutorials.chatroomapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipleScreen(navController: NavController) {


    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }
    val backgroundImage: Painter = painterResource(id = R.drawable.ucbg)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )


        val categories = listOf("ALP", "BVB", "GANGA", "LBS", "AKK")
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            items(categories) { cat ->
                BrowserItem(navController, cat)
            }
        }
    }
}

@Composable
fun BrowserItem(navController: NavController, cat: String) {
    // Map of category images
    val categoryImages = mapOf(
        "ALP" to R.drawable.alp,
        "BVB" to R.drawable.bvb,
        "GANGA" to R.drawable.ganga,
        "LBS" to R.drawable.lbs,
        "AKK" to R.drawable.akk
    )

    // Get drawable from the map or use a default
    val drawable = categoryImages[cat] ?: R.drawable.ucbg

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable { navigateToScreen(navController, cat) } // Call navigateToScreen here
    ) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = cat,
            modifier = Modifier.fillMaxWidth().padding(16.dp) // Adjust the size if needed
        )
    }
}

fun navigateToScreen(navController: NavController, cat: String) {
    when (cat) {
        "BVB" -> navController.navigate(Screen.BVBHOSTEL.route)
        "LBS" -> navController.navigate(Screen.LBSHOSTEL.route)
        "AKK"-> navController.navigate(Screen.AKKAM.route)
        "ALP"-> navController.navigate(Screen.ALPHOSTEL.route)
        "GANGA"-> navController.navigate(Screen.GANGA.route)



        // Add other cases if needed
    }
}
