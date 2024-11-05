package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.graphics.Color
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun ScreenFilmsDetails(viewModel: MainViewModel, filmId: Int, navController: NavController) {

    LaunchedEffect(filmId) {
        viewModel.getFilmbyId(filmId)
    }

    val movie by viewModel.selectedFilm.collectAsState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    val columns = when (classWidth) {
        WindowWidthSizeClass.COMPACT -> 2
        else -> 4
    }

    movie?.let { filmDetails ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            if (filmDetails.poster_path != null) {
                item {
                    if (filmDetails.backdrop_path != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${filmDetails.backdrop_path}",
                            contentDescription = "Image du film",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${filmDetails.poster_path}",
                            contentDescription = "Image du film",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
            item {
                Text(
                    text = filmDetails.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                Row(modifier = Modifier.padding(16.dp)) {
                    if (filmDetails.poster_path != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${filmDetails.poster_path}",
                            contentDescription = "Image du film",
                            modifier = Modifier
                                .width(150.dp)
                                .height(225.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.paysage),  // Image locale dans drawable
                            contentDescription = "Description de l'image",
                            modifier = Modifier
                                .width(150.dp)
                                .height(225.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (filmDetails.release_date != null) {
                            Text(text = "Sortie le ${filmDetails.release_date}")
                        }
                        Text(text = filmDetails.genres.joinToString { it.name })
                        Text(text = "Popularité : ${filmDetails.popularity}")
                    }
                }
            }
            item {
                Text(
                    text = "Synopsis",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Text(
                    text = filmDetails.overview,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Text(
                    text = "Distribution",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val acteurs = filmDetails.credits.cast


                    for (i in acteurs.indices step columns) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly // Espacement uniforme
                        ) {
                            // Ajouter un acteur à la ligne
                            for (j in 0 until columns) {
                                if (i + j < acteurs.size) {
                                    val acteur = acteurs[i + j]
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .weight(1f) // Faire en sorte que chaque carte prenne le même espace
                                            .clickable {
                                                navController.navigate("ActeurDetails/${acteur.id}")
                                            },
                                        colors = CardDefaults.cardColors(
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            if (acteur.profile_path != null) {
                                                AsyncImage(
                                                    model = "https://image.tmdb.org/t/p/w780${acteur.profile_path}",
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
                                            Text(
                                                text = acteur.name,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}