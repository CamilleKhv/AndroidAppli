package com.example.premiereappli_camillekerherve

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.AsyncImage

@Composable
fun ScreenExam(viewModel: MainViewModel= viewModel()){
    val collections by viewModel.listcollections.collectAsState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    val columns = when (classWidth) {
        WindowWidthSizeClass.COMPACT -> 2
        WindowWidthSizeClass.MEDIUM -> 3
        else -> 4
    }

    Column {
        Text(
            "  Liste des Collections",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        LaunchedEffect(Unit) {
            viewModel.getCollection()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns)
        ) {
            items(collections) { collection ->
                CollectionItem(collection = collection)
            }
        }
    }
}


@Composable
fun CollectionItem(collection: ResultCollection) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val classWidth = windowSizeClass.windowWidthSizeClass

    Card(
        modifier = Modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(241, 119, 119, 255),
            contentColor = Color.Black
        )
    )
    {
        Column {
            if (collection.poster_path != null) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${collection.poster_path}",
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
            Text(text = collection.name)
        }
    }
}