package eu.tutorials.chatroomapp.viewmodel



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.tutorials.chatroomapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit= {}
){
    //TO COVER THE WHOLE STATUS BAR WITH THE TOP BAR U NEED THE ... LINES OF CODES
    val systemUiController = rememberSystemUiController()//...
    val statusBarColor = Color.Black//..
    systemUiController.setStatusBarColor(
        color = statusBarColor,
        darkIcons = true // Adjust based on your status bar color
    ) //..

    val navigationIcon : (@Composable () -> Unit)? =
        if(!title.contains("Problems")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }else{
            null
        }


    CenterAlignedTopAppBar(
        title = {
            Text(text = title,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .heightIn(max = 50.dp))

        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.black)
        ),
        navigationIcon = {
            // Provide an empty composable if navigationIcon is null
            navigationIcon?.invoke() ?: Spacer(modifier = Modifier)
        },
        modifier = Modifier.height(80.dp)
    )
}