package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ScreenFilms(viewModel: MainViewModel= viewModel(), navController: NavController, classWidth: WindowWidthSizeClass) {
    val movies by viewModel.listmovies.collectAsState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    val columns = when (classWidth) {
        WindowWidthSizeClass.COMPACT -> 2
        else -> 3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Column {
            Text(
                "Liste des films",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            LaunchedEffect(Unit) {
                viewModel.getMovies()
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, navController = navController)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: ModelMovies, navController: NavController){
    Column(modifier = Modifier
        .padding(4.dp)
        .clickable {
            navController.navigate("filmDetails/${movie.id}")
        }
        .background(Color(0xFFFFFFFF))
        .border(
            BorderStroke(2.dp, Color(0xFFBBD2E1)), RoundedCornerShape(4.dp)
        )
        .padding(8.dp)
    ) {
        if (movie.poster_path != null) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${movie.poster_path}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            )
        }else{
            Image(
                painter = painterResource(R.drawable.paysage),  // Image locale dans drawable
                contentDescription = "Description de l'image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(movie.title)
    }
}