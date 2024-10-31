package com.example.premiereappli_camillekerherve


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.premiereappli_camillekerherve.ui.theme.PremiereAppli_CamilleKerherveTheme
import kotlinx.serialization.Serializable

@Serializable
class HomeDest

@Serializable
class FilmsDest

@Serializable
class ActeursDest

@Serializable
class SeriesDest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PremiereAppli_CamilleKerherveTheme {
                BarreNavigation()
            }
        }
    }
}

@Composable
fun BarreNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val viewModel: MainViewModel = viewModel()

    val classWidth = windowSizeClass.windowWidthSizeClass

    Scaffold(
        topBar = {
            if (currentDestination?.hasRoute<HomeDest>() != true) {
                when (classWidth) {
                    WindowWidthSizeClass.COMPACT ->
                        CompactTopBar(navController, currentDestination, viewModel)
                    //else -> ExpandedTopBar(currentDestination, viewModel)
                }
            }
        },
        bottomBar = {
            if (currentDestination?.hasRoute<HomeDest>() != true) {
                when (classWidth) {
                    WindowWidthSizeClass.COMPACT ->
                        BottomBar(navController, currentDestination)
                    else ->{}
                }
            }
        },
    ) { innerPadding ->
        Row {
            if (currentDestination?.hasRoute<HomeDest>() != true) {
                when (classWidth) {
                    WindowWidthSizeClass.COMPACT -> {
                    }

                    else -> {
                        Column(modifier = Modifier.padding(innerPadding)) {
                            SideBar(navController, currentDestination)
                        }
                    }
                }
            }
        }
        Column {
            NavHost(
                navController = navController, startDestination = HomeDest(),
                Modifier.padding(innerPadding)
                    .fillMaxSize()
            ) {
                composable<HomeDest> { Screen(navController, windowSizeClass) }
                composable<FilmsDest> { ScreenFilms(viewModel, navController, classWidth) }
                composable<ActeursDest> { ScreenActeurs(viewModel, navController) }
                composable<SeriesDest> { ScreenSeries(viewModel, navController) }

                composable(
                    "filmDetails/{filmId}",
                    arguments = listOf(navArgument("filmId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val filmId = backStackEntry.arguments?.getInt("filmId")
                    filmId?.let {
                        ScreenFilmsDetails(
                            viewModel = viewModel,
                            filmId = it,
                            navController
                        )
                    }
                }

                composable(
                    "serieDetails/{serieId}",
                    arguments = listOf(navArgument("serieId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val serieId = backStackEntry.arguments?.getInt("serieId")
                    serieId?.let {
                        ScreenSeriesDetails(
                            viewModel = viewModel,
                            serieId = it,
                            navController
                        )
                    }
                }

                composable(
                    "acteurDetails/{actorId}",
                    arguments = listOf(navArgument("actorId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val actorId = backStackEntry.arguments?.getInt("actorId")
                    actorId?.let {
                        ScreenActeursDetails(
                            viewModel = viewModel,
                            acteurId = it
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactTopBar( navController: NavController, currentDestination: NavDestination?, viewModel: MainViewModel) {
    var searchActive by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(currentDestination) {
        if (currentDestination?.hasRoute<HomeDest>() == true) {
            searchActive = false
        }
    }

    TopAppBar(
        title = {
            when {
                // Cas 1 : Affiche uniquement le texte sur les pages de détails
                currentDestination?.route == "filmDetails/{filmId}"
                        || currentDestination?.route == "serieDetails/{serieId}"
                        || currentDestination?.route == "acteurDetails/{actorId}" -> {
                    Text(text = "Cinéphile App")
                }
                // Cas 2 : Affiche la SearchBar si la recherche est active
                searchActive -> {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        query = text,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "recherche"
                            )
                        },
                        onQueryChange = { text = it },
                        onSearch = {
                            // Lance la recherche et ferme la barre de recherche
                            searchActive = false
                            when {
                                currentDestination?.hasRoute<FilmsDest>() == true -> {
                                    viewModel.getFilmsbySearch(it)
                                }

                                currentDestination?.hasRoute<ActeursDest>() == true -> {
                                    viewModel.getActeursbySearch(it)
                                }

                                currentDestination?.hasRoute<SeriesDest>() == true -> {
                                    viewModel.getSeriesbySearch(it)
                                }
                            }
                        },
                        active = searchActive,
                        onActiveChange = { searchActive = it },
                        placeholder = {
                            Text(text = "Recherchez...")
                        }
                    ) {}
                }
                // Cas 3 : Affiche le texte par défaut si la recherche n'est pas active
                else -> {
                    Text(text = "Cinéphile App")
                }
            }
        },
        actions = {
            if (currentDestination?.route == "filmDetails/{filmId}"
                || currentDestination?.route == "serieDetails/{serieId}"
                || currentDestination?.route == "acteurDetails/{actorId}"
            ) {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "Retour"
                    )
                }
            } else {
                IconButton(onClick = {
                    if (searchActive) {
                        searchActive = false // Ferme la barre de recherche
                    } else {
                        searchActive = true // Ouvre la barre de recherche
                    }
                }) {
                    Icon(
                        imageVector = if (searchActive) Icons.Rounded.ArrowBack else Icons.Rounded.Search,
                        contentDescription = if (searchActive) "Fermer la barre de recherche" else "Ouvrir la barre de recherche"
                    )
                }
            }
        }
    )
}

@Composable
fun ExpandedTopBar(currentDestination: NavDestination?, viewModel: MainViewModel) {
}



@Composable
fun BottomBar(navController: NavController, currentDestination: NavDestination?) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.operateur_camera),
                    contentDescription = "Logo Films",
                    modifier = Modifier.size(25.dp)
                )
            },
            label = { Text("Films") },
            selected = currentDestination?.hasRoute<FilmsDest>() == true,
            onClick = { navController.navigate(FilmsDest()) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.smart),
                    contentDescription = "Logo series",
                    modifier = Modifier.size(25.dp)
                )
            },
            label = { Text("Séries") },
            selected = currentDestination?.hasRoute<SeriesDest>() == true,
            onClick = { navController.navigate(SeriesDest()) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.personne_debout),
                    contentDescription = "Logo acteurs",
                    modifier = Modifier.size(25.dp)
                )
            },
            label = { Text("Acteurs") },
            selected = currentDestination?.hasRoute<ActeursDest>() == true,
            onClick = { navController.navigate(ActeursDest()) }
        )
    }
}

@Composable
fun SideBar(navController: NavController, currentDestination: NavDestination?) {
    Column(modifier = Modifier.fillMaxHeight()) {
        NavigationRail(containerColor = Color.Yellow){
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.operateur_camera),
                        contentDescription = "Logo Films",
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = { Text("Films") },
                selected = currentDestination?.hasRoute<FilmsDest>() == true,
                onClick = { navController.navigate(FilmsDest()) }
            )
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.smart),
                        contentDescription = "Logo series",
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = { Text("Séries") },
                selected = currentDestination?.hasRoute<SeriesDest>() == true,
                onClick = { navController.navigate(SeriesDest()) }
            )
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.personne_debout),
                        contentDescription = "Logo acteurs",
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = { Text("Acteurs") },
                selected = currentDestination?.hasRoute<ActeursDest>() == true,
                onClick = { navController.navigate(ActeursDest()) }
            )
        }
    }
}
