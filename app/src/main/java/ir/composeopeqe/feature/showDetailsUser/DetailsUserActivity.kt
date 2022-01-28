package ir.composeopeqe.feature.showDetailsUser

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import ir.composeopeqe.R
import ir.composeopeqe.common.EXTRA_KEY
import ir.composeopeqe.data.Result
import ir.composeopeqe.ui.theme.ComposeOpeqeTheme
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class DetailsUser : ComponentActivity() {

    private var user : Result ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeOpeqeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    try{
                        user = intent.getParcelableExtra(EXTRA_KEY)
                    }
                    catch (e : Exception){
                        Toast.makeText(this , "error" , Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    if (user != null)
                        user?.let { GreetingDetails(it) }
                }
            }
        }
    }
}


@Composable
fun GreetingDetails(user : Result) {

    MaterialTheme {

        Image(
            painter = rememberImagePainter(user.picture.large),
            contentDescription = "" ,
            modifier = Modifier
                .fillMaxSize() ,
            alignment = Alignment.TopCenter ,
            alpha = 0.2f
        )

        btnBack()

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp) ,
            shape = RoundedCornerShape(topStart = 65.dp , topEnd = 65.dp) ,
            elevation = 15.dp
        ) {

            Column {
                val fullName = user.name.first + " " + user.name.last
                val location = user.location.state + "-" + user.location.city + "-" + user.location.street
                Text(
                    text = fullName ,
                    fontSize = 23.sp ,
                    fontWeight = FontWeight.W600 ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 130.dp) ,
                    textAlign = TextAlign.Center
                )

                Column (
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ){
                    Box(modifier = Modifier.padding(top = 50.dp))
                    itemsProfile(title = stringResource(id = R.string.email), value = user.email, icon = R.drawable.ic_email)
                    itemsProfile(title = stringResource(id = R.string.phone), value = user.phone, icon = R.drawable.ic_phone)
                    itemsProfile(title = stringResource(id = R.string.cell), value = user.cell, icon = R.drawable.ic_phone)
                    itemsProfile(title = stringResource(id = R.string.location), value = location , icon = R.drawable.ic_location)
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp)
        ) {
            Card(
                modifier = Modifier.padding(top = 20.dp),
                shape = RoundedCornerShape(45.dp) ,
                elevation = 15.dp ,
                border = BorderStroke(3.dp , Brush.linearGradient(listOf(Color.Red , Color.Blue)))
            ) {
                Image(
                    painter = rememberImagePainter(user.picture?.large),
                    contentDescription = "" ,
                    Modifier.size(170.dp) ,
                    alignment = Alignment.Center
                )
            }
        }
    }
}


@Composable
fun itemsProfile(title : String , value : String , icon : Int){

    Row(
        modifier = Modifier.padding(start = 15.dp , top = 25.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "" ,
            tint = Color.White ,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Red)
                .padding(10.dp)
        )

        Column(
            Modifier.padding(start = 15.dp)
        ) {
            Text(
                text = title ,
                fontSize = 14.sp ,
                color = Color.Gray
            )

            Text(
                text = value ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun btnBack(){

    val coroutineScope = rememberCoroutineScope()
    val activity = (LocalContext.current as? Activity)

    Column(
        horizontalAlignment = Alignment.Start ,
        verticalArrangement = Arrangement.Top ,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        FloatingActionButton(
            modifier = Modifier.size(45.dp) ,
            onClick = {
                coroutineScope.launch { activity?.finish() }
            } ,
            backgroundColor = Color.White
        ) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_arrow_back_24) ,
                contentDescription = "" , tint = Color.Black
            )
        }
    }
}
