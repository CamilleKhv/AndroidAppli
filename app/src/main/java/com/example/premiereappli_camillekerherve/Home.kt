package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass


@Composable
fun Screen(navHostController: NavHostController, classes: WindowSizeClass) {
    val classWidth = classes.windowWidthSizeClass
    if (classWidth == WindowWidthSizeClass.COMPACT
        || classWidth == WindowWidthSizeClass.MEDIUM
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageProfile()
            Spacer(modifier = Modifier.height(7.dp))
            TitleText()
            Spacer(modifier = Modifier.height(25.dp))
            Description()
            Spacer(modifier = Modifier.height(45.dp))
            UsefulLink()
            Spacer(modifier = Modifier.height(55.dp))
            SearchButton(navHostController)
        }
    } else {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(70.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageProfile()
                Spacer(modifier = Modifier.height(5.dp))
                TitleText()
                Spacer(modifier = Modifier.height(5.dp))
                Description()
            }
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UsefulLink()
                Spacer(modifier = Modifier.height(40.dp))
                SearchButton(navHostController)
            }
        }


    }
}

@Composable
fun ImageProfile() {
    Box(
        modifier = Modifier
            .size(125.dp)
    )
    {
        Image(
            painter = painterResource(R.drawable.hanji_zoe),
            contentDescription = "Description de l'image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    BorderStroke(4.dp, Color(94, 31, 31, 255)),
                    CircleShape,
                )
        )
    }
}

@Composable
fun TitleText() {
    Text(
        "Hanji Zoë",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Description() {
    Text(
        "Major du bataillon d'exploration",
        fontStyle = FontStyle.Italic,
        modifier = Modifier.padding(2.dp)
    )
    Text(
        "L'attaque des titans",
        fontStyle = FontStyle.Italic,
    )
}

@Composable
fun UsefulLink() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)

    ) {
        Row(
            modifier = Modifier.align(Alignment.Start)

        ) {
            Image(
                painter = painterResource(R.drawable.gmail_logo),
                contentDescription = "Logo Gmail",
                modifier = Modifier
                    .size(width = 30.dp, height = 30.dp)
            )
            Text(" hanji.zoe@gmail.com")
        }
        Row(
            modifier = Modifier.align(Alignment.Start)

        ) {
            Image(
                painter = painterResource(R.drawable.linkedin_logo),
                contentDescription = "Logo linkedin",
                modifier = Modifier
                    .size(width = 25.dp, height = 25.dp)
            )
            Text("  www.linkedin.com/in/HanjiZoeMajor")
        }
    }
}


@Composable
fun SearchButton(navController: NavHostController) {
    Button(
        onClick = { navController.navigate(FilmsDest()) },
        modifier = Modifier.size(width = 150.dp, height = 50.dp),
    ) {
        Text(text = "Démarrer", fontSize = 17.sp)
    }
}