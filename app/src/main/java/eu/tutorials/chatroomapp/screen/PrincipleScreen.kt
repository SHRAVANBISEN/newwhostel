package eu.tutorials.chatroomapp.screen

import AuthViewModel
import WishViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PrincipleScreen(
    navController: NavController,
    viewModel: WishViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {

    // Set up the system UI controller for status bar colors
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }

    // Scaffold layout with a top bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.black)
                ),

                title = {
                    androidx.compose.material.Text(
                        text = "HOSTELS",
                        color = colorResource(id = R.color.white)
                    )
                },
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.DefaultScreen.route) {
                            popUpTo(Screen.BVBHOSTEL.route) { inclusive = true }
                            popUpTo(0) // Ensure to pop to the root
                        }
                    }) {
                        androidx.compose.material.Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { paddingValues -> // Pass padding from scaffold to content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Gray, Color.Black)
                        )
                    )
                    .padding(paddingValues) // Apply padding from the scaffold content
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top // This ensures that the Lazy Grid doesn't take all space
                ) {
                    val categories = listOf("ALP", "BVB", "GANGA", "LBS", "AKK")

                    // Lazy Grid Layout for displaying the items
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1), // One column as in the original code
                        modifier = Modifier
                            .weight(1f) // Give Lazy Grid a weight so it doesn't fill all space
                            .padding(16.dp), // Spacing for better aesthetics
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categories) { cat ->
                            BrowserItem(navController, cat)
                        }
                    }

                    // Button for room details
                    Button(
                        onClick = { navController.navigate(Screen.RoomDet.route) },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Room Details")
                    }
                }
            }
        }
    )
}


@SuppressLint("UnusedCrossfadeTargetStateParameter")
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

    // Add animation on click
    var clicked by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (clicked) 1.1f else 1f, // Scale up when clicked
        animationSpec = tween(durationMillis = 300)
    )

    // Card with shadow and corner rounding for better look
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clickable {
                clicked = !clicked
                navigateToScreen(navController, cat)
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                )
        ) {
            // Image with Crossfade animation
            Crossfade(targetState = clicked) {

            }

            // Category Text
            Text(
                text = cat,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun navigateToScreen(navController: NavController, cat: String) {
    when (cat) {
        "BVB" -> navController.navigate(Screen.BVBHOSTEL.route)
        "LBS" -> navController.navigate(Screen.LBSHOSTEL.route)
        "AKK" -> navController.navigate(Screen.AKKAM.route)
        "ALP" -> navController.navigate(Screen.ALPHOSTEL.route)
        "GANGA" -> navController.navigate(Screen.GANGA.route)
        // Add other cases if needed
    }
}
