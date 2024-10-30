package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ScreenSeriesDetails(viewModel: MainViewModel, serieId: Int, navController: NavController) {

    LaunchedEffect(serieId) {
        viewModel.getSeriebyId(serieId)
    }

    val serie by viewModel.selectedSerie.collectAsState()

    serie?.let { serieDetails ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (serieDetails.poster_path != null) {
                item {
                    if (serieDetails.backdrop_path != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${serieDetails.backdrop_path}",
                            contentDescription = "Image de la série",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${serieDetails.poster_path}",
                            contentDescription = "Image de la série",
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
                    text = serieDetails.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Row(modifier = Modifier.padding(16.dp)) {
                    if (serieDetails.poster_path != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${serieDetails.poster_path}",
                            contentDescription = "Image du film",
                            modifier = Modifier
                                .width(150.dp)
                                .height(225.dp)
                        )
                    }else{
                        Image(
                            painter = painterResource(R.drawable.paysage),  // Image locale dans drawable
                            contentDescription = "Description de l'image",
                            modifier = Modifier
                                .width(150.dp)
                                .height(225.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (serieDetails.created_by.isNotEmpty()) {
                            (Text(text = "Réalisé par ${serieDetails.created_by.joinToString { it.name }}"))
                        }
                        //Text(text="${serieDetails.id}")
                        Text(text = "${serieDetails.genres.joinToString { it.name }}")
                        Text(text = "Nombre de saisons : ${serieDetails.number_of_seasons}")
                        Text(text = "Nombre d'épisodes : ${serieDetails.number_of_episodes}")

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
                    text = serieDetails.overview,
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
                    val acteurs = serieDetails.credits.cast

                    for (i in acteurs.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            // Premier acteur de la ligne
                            val acteur1 = acteurs[i]
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        navController.navigate("ActeurDetails/${acteur1.id}")
                                    }
                                    .padding(8.dp)
                            ) {
                                if (acteur1.profile_path != null){
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w780${acteur1.profile_path}",
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }else{
                                    Image(
                                        painter = painterResource(R.drawable.paysage),
                                        contentDescription = "Description de l'image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )
                                }
                                Text(
                                    text = acteur1.name,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }

                            if (i + 1 < acteurs.size) {
                                val acteur2 = acteurs[i + 1]
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            navController.navigate("ActeurDetails/${acteur2.id}")
                                        }
                                        .padding(8.dp)
                                ) {
                                    if (acteur2.profile_path != null){
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w780${acteur2.profile_path}",
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }else{
                                        Image(
                                            painter = painterResource(R.drawable.paysage),
                                            contentDescription = "Description de l'image",
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }
                                    Text(
                                        text = acteur2.name,
                                        modifier = Modifier.padding(2.dp)
                                    )
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