package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ScreenActeursDetails(viewModel: MainViewModel, acteurId: Int) {

    LaunchedEffect(acteurId) {
        viewModel.getActeurbyId(acteurId)
    }

    val actor by viewModel.selectedActeur.collectAsState()

    actor?.let { acteurDetails ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = acteurDetails.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Row(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780${acteurDetails.profile_path}",
                        contentDescription = "Image de l'actor",
                        modifier = Modifier
                            .width(150.dp)
                            .height(225.dp)
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (acteurDetails.birthday != null) {
                            Text(text = "Né(e) le ${acteurDetails.birthday}")
                        }
                        if (acteurDetails.place_of_birth != null){
                            Text(text = "Originaire de ${acteurDetails.place_of_birth}")
                        }
                        if (acteurDetails.deathday != null) {
                            Text(text = "mort le' ${acteurDetails.deathday}")
                        }
                        Text(text = "Connu pour : ${acteurDetails.known_for_department}")
                        Text(text = "Popularité : ${acteurDetails.popularity}")
                    }
                }
            }
            item {
                Text(
                    text = "Biographie",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (acteurDetails.biography !="") {
                item {
                    Text(acteurDetails.biography)
                }
            } else {
                item {
                    Text(text = "Pas d'information")
                }
            }

            /*
            item {
                Text(
                    text = "Filmographie",
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
                    val films = acteurDetails.creditsActeur.castActeur.take(6)
                    for (film in films) {
                        Column(modifier = Modifier
                            .clickable {
                                navController.navigate("filmDetails/${film.id}")
                            }
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w780${film.poster_path}",
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                            )
                            Text(
                                text = film.title,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }

                }
            }
           */
        }
    }
}