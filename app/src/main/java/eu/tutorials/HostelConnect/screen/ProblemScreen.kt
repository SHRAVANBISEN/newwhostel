package eu.tutorials.HostelConnect.screen

import WishViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.HostelConnect.R
import eu.tutorials.HostelConnect.Screen
import eu.tutorials.HostelConnect.data.Wish
import eu.tutorials.HostelConnect.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isUserLoggedIn by authViewModel.isUserLoggedIn.observeAsState(initial = false)
    val isLoading by authViewModel.isLoading.observeAsState(initial = false)
    val wishes by viewModel.wishes.collectAsStateWithLifecycle(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val authResult by authViewModel.authResult.observeAsState()

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.loadWishes()
    }

    val context = LocalContext.current

    BackHandler {
        (context as? android.app.Activity)?.finish()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.black)
                ),
                title = { Text(text = "Problems", color = colorResource(id = R.color.white)) },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navController.navigate(Screen.DefaultScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { inclusive = true }
                            popUpTo(0) // Ensure to pop to the root
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                backgroundColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val wishlist by viewModel.wishes.collectAsState()
                LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
                    items(wishlist) { wish ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                    viewModel.deleteWish(wish)
                                }
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color by animateColorAsState(
                                    if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Black else Color.Transparent , label = ""
                                )
                                val alignment = Alignment.CenterEnd
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(Icons.Default.Delete,
                                        contentDescription = "Delete Icon",
                                        tint = Color.White)
                                }
                            },
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { FractionalThreshold(1f) },
                            dismissContent = {
                                WishItem(wish = wish) {
                                    val id = wish.id
                                    navController.navigate(Screen.AddScreen.route + "/$id")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp,
        backgroundColor = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = wish.title, style = MaterialTheme.typography.h6)
            Text(text = wish.description, style = MaterialTheme.typography.body1)
        }
    }
}

