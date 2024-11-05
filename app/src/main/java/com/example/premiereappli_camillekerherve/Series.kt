package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun ScreenSeries(viewModel: MainViewModel= viewModel(), navController: NavController, classWidth: WindowWidthSizeClass) {
    val series by viewModel.listseries.collectAsState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    val columns = when (classWidth) {
        WindowWidthSizeClass.COMPACT -> 2
        else -> 4
    }

    Column {
        Text(
            "Liste des Séries",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        LaunchedEffect(Unit) {
            viewModel.getSeries()

        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns)

        ) {
            items(series) { serie ->
                SeriesItem(serie = serie, navController = navController)
            }
        }
    }
}

@Composable
fun SeriesItem(serie: ModelSeries, navController: NavController) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("serieDetails/${serie.id}")
            },
        colors = CardDefaults.cardColors(
            //containerColor = Color(0xFFb3ff7a) ,
            contentColor = Color.Black
        )
    )
    {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            when (classWidth) {
                WindowWidthSizeClass.COMPACT ->
                    if (serie.poster_path != null) {

                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${serie.poster_path}",
                            contentDescription = null,
                            modifier = Modifier

                                .fillMaxSize()
                                .padding(4.dp),
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.paysage),
                            contentDescription = "Description de l'image",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                else ->
                    if (serie.poster_path != null) {

                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${serie.poster_path}",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.paysage),
                            contentDescription = "Description de l'image",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
            }
            Text(serie.name)
        }
    }
}