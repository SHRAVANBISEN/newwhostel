package eu.tutorials.chatroomapp.screen

import AuthViewModel
import WishViewModel
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.R
import eu.tutorials.chatroomapp.Screen
import eu.tutorials.chatroomapp.data.Result
import eu.tutorials.chatroomapp.data.Wish

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BVBVIEW(
    navController: NavController,
    viewModel: WishViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoading by authViewModel.isLoading.observeAsState(initial = false)
    val wishes by viewModel.wishes.collectAsStateWithLifecycle(emptyList())

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color.Black
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = true
        )
    }

    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    BackHandler  {
        (context as? android.app.Activity)?.finish()
    }

    LaunchedEffect(Unit) {
        viewModel.loadWishesByHostel("BVB")
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
                navigationIcon = {
                    IconButton(onClick = {

                        navController.navigate(Screen.PrincipleScreen.route) {
                            popUpTo(Screen.BVBHOSTEL.route) { inclusive = true }
                            popUpTo(0) // Ensure to pop to the root
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back", tint = Color.White)
                    }
                },
                title = { Text(text = "BVB Problems", color = colorResource(id = R.color.white)) },

            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
                    items(wishes) { wish ->
                        BVBWishItem(wish = wish) {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BVBWishItem(wish: Wish, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            ,
        elevation = 8.dp,
        backgroundColor = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Room No : ${wish.roomnum}", style = MaterialTheme.typography.h6 , textAlign = TextAlign.Center)

            Text(text = wish.title, style = MaterialTheme.typography.h6 , fontWeight = FontWeight.Bold)
            Text(text = wish.description, style = MaterialTheme.typography.body1)
        }
    }
}
