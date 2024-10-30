package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun ScreenActeurs(viewModel: MainViewModel= viewModel(), navController: NavController) {

    val actors by viewModel.listactors.collectAsState()

    Column {
        Text(
            "Liste des acteurs",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        LaunchedEffect(Unit) {
            viewModel.getActors()

        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)

        ) {
            items(actors) { actor ->
                ActorItem( actor, navController = navController)
            }
        }
    }
}

@Composable
fun ActorItem(actor: ModelActeurs, navController: NavController){
    Column(modifier = Modifier
        .clickable {
            navController.navigate("ActeurDetails/${actor.id}")
        }
    ) {
        if (actor.profile_path != null) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${actor.profile_path}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            )
        }else{
            Image(
                painter = painterResource(R.drawable.paysage),
                contentDescription = "Description de l'image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(actor.name,
            modifier = Modifier.padding(2.dp)
        )
    }
}