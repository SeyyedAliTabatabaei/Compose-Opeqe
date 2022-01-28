package ir.composeopeqe.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dagger.hilt.android.AndroidEntryPoint
import ir.composeopeqe.R
import ir.composeopeqe.common.AnimatedShimmer
import ir.composeopeqe.common.EXTRA_KEY
import ir.composeopeqe.data.Result
import ir.composeopeqe.feature.showDetailsUser.DetailsUser
import ir.composeopeqe.ui.theme.ComposeOpeqeTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            ComposeOpeqeTheme {
                Surface(color = MaterialTheme.colors.primary) {
                    GreetingMain(viewModel)
                }
            }
        }
    }
}

@Composable
fun GreetingMain(viewModel : MainViewModel) {
    MaterialTheme {

        var textSearch by remember { mutableStateOf("") }
        val stateScroll = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val list : State<List<Result>> = viewModel.listUsers.collectAsState(listOf())
        val loading by viewModel.showLoading.collectAsState()
        val errorConnection by viewModel.showError.collectAsState()


        Column(
            Modifier.background(Color.White)
        ) {

            Text(
                text = "Users",
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp) ,
                textAlign = TextAlign.Center ,
                color = MaterialTheme.colors.onBackground ,
                fontStyle = FontStyle.Italic
            )

            TextField(
                value = textSearch,
                onValueChange = { textSearch = it } ,
                label = {
                    Text(text = stringResource(id = R.string.search_hint))
                        } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 30.dp),
                singleLine = true ,
                shape = RoundedCornerShape(20.dp) ,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search , contentDescription = null)
                } ,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.gray),
                    focusedIndicatorColor =  Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent ,
                    textColor = Color.Black
                )
            )

            if (loading && !errorConnection){
                LazyColumn{
                    item {
                        for (i in 0..20)
                            AnimatedShimmer()
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxSize() ,
                backgroundColor = Color.White
            ) {

                if (textSearch.isEmpty()){
                    LazyColumn(state = stateScroll){
                        items(list.value){ user ->
                            itemUser(user = user)
                        }
                    }
                }
                else{
                    LazyColumn(state = stateScroll){
                        items(list.value){ user ->
                            val nameUser = user.name.title + " " + user.name.first + " " + user.name.last
                            if (nameUser.lowercase().contains(textSearch.lowercase()))
                                itemUser(user = user)
                        }
                    }
                }
            }
        }



        if (stateScroll.firstVisibleItemIndex != 0)
            Column(
                horizontalAlignment = Alignment.End ,
                verticalArrangement = Arrangement.Bottom ,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                FloatingActionButton(
                    modifier = Modifier.size(55.dp) ,
                    onClick = {
                        coroutineScope.launch { stateScroll.animateScrollToItem(0) }
                    } ,
                    backgroundColor = Color.Red
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp ,
                        contentDescription = "" , tint = Color.White
                    )
                }
            }



        if (errorConnection)
            errorConnection()

    }
}


@Composable
fun errorConnection(){
    val animation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_error))

    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = animation ,
            isPlaying = true ,
            restartOnPlay = true ,
            modifier = Modifier.fillMaxSize(fraction = 0.3f) ,
            alignment = Alignment.Center
        )
        Text(
            text = stringResource(id = R.string.error_connection) ,
            fontSize = 23.sp ,
            fontWeight = FontWeight.Bold ,
            color = Color.Black
        )
    }
}


@Composable
fun itemUser(user : Result){

    val nameUser = user.name.title + " " + user.name.first + " " + user.name.last
    val context = LocalContext.current
    val userr = user

    Card(
        modifier = Modifier
            .clickable {
                val intent = Intent(context , DetailsUser::class.java)
                intent.putExtra(EXTRA_KEY , userr)
                context.startActivity(intent)
            },
        elevation = 0.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 10.dp, bottom = 10.dp, end = 30.dp)
        ) {
            Image(
                painter = rememberImagePainter(user.picture.medium ),
                contentDescription = "" ,
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(17.dp)) ,
                alignment = Alignment.Center
            )

            Text(
                text = nameUser ,
                fontSize = 16.sp ,
                modifier = Modifier.padding(start = 15.dp , top = 12.dp)
                )
        }
    }
}

