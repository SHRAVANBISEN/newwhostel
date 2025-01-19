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
import androidx.compose.ui.draw.clip
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

    // System UI Controller for status bar
    val systemUiController = rememberSystemUiController()
    val statusBarColor = colorResource(id = R.color.black)
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    // Scaffold with a top bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.black)
                ),
                title = {
                    Text(
                        text = "Hostels",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.DefaultScreen.route) {
                            popUpTo(Screen.DefaultScreen.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = colorResource(id = R.color.black),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.DarkGray,
                            Color.Black
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val categories = listOf("ALP", "BVB", "GANGA", "LBS", "AKK")

                // Lazy Grid for Categories
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Use two columns for a better look
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { cat ->
                        BrowserItem(navController, cat)
                    }
                }

                // Room Details Button
                Button(
                    onClick = { navController.navigate(Screen.RoomDet.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white), // Use containerColor instead of backgroundColor
                        contentColor = colorResource(id = R.color.black)
                    )
                ) {
                    Text(
                        text = "Room Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun BrowserItem(navController: NavController, cat: String) {
    val categoryImages = mapOf(
        "ALP" to R.drawable.alp,
        "BVB" to R.drawable.bvb,
        "GANGA" to R.drawable.ganga,
        "LBS" to R.drawable.lbs,
        "AKK" to R.drawable.akk
    )

    val drawable = categoryImages[cat] ?: R.drawable.ucbg

    var clicked by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (clicked) 1.1f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clickable {
                clicked = !clicked
                navigateToScreen(navController, cat)
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.black)
        )
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
            Image(
                painter = painterResource(id = drawable),
                contentDescription = cat,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Text(
                text = cat,
                fontSize = 16.sp,
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
    }
}

