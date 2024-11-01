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
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.material3.FloatingActionButton
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

                    else -> {}
                }
            }
        },
        floatingActionButton = {
            if (classWidth != WindowWidthSizeClass.COMPACT && currentDestination?.hasRoute<HomeDest>() != true) {
                SearchExpanded(navController, currentDestination, viewModel)
//                FloatingActionButton(
//                    onClick = { searchActive = !searchActive }
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.Search,
//                        contentDescription = "Ouvrir la barre de recherche"
//                    )
//                }
            }
        },
        bottomBar = {
            if (currentDestination?.hasRoute<HomeDest>() != true) {
                when (classWidth) {
                    WindowWidthSizeClass.COMPACT -> BottomBar(navController, currentDestination)
                    else -> {}
                }
            }
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (classWidth != WindowWidthSizeClass.COMPACT && currentDestination?.hasRoute<HomeDest>() != true) {
                SideBar(navController, currentDestination)
            }
            Column(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController, startDestination = HomeDest(),
                    Modifier.fillMaxSize()
                ) {
                    composable<HomeDest> { Screen(navController, windowSizeClass) }
                    composable<FilmsDest> { ScreenFilms(viewModel, navController, classWidth) }
                    composable<ActeursDest> { ScreenActeurs(viewModel, navController, classWidth) }
                    composable<SeriesDest> { ScreenSeries(viewModel, navController, classWidth) }

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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarAppli(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit
) {
    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        query = query,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "recherche"
            )
        },
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch(it) // Lance la recherche
            onActiveChange(false) // Ferme la barre de recherche après la recherche
        },
        active = active,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(text = "Recherchez...")
        }
    ){}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactTopBar(
    navController: NavController,
    currentDestination: NavDestination?,
    viewModel: MainViewModel
) {
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
                    SearchBarAppli(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = { query ->
                            when {
                                currentDestination?.hasRoute<FilmsDest>() == true -> {
                                    viewModel.getFilmsbySearch(query)
                                }
                                currentDestination?.hasRoute<ActeursDest>() == true -> {
                                    viewModel.getActeursbySearch(query)
                                }
                                currentDestination?.hasRoute<SeriesDest>() == true -> {
                                    viewModel.getSeriesbySearch(query)
                                }
                            }
                        },
                        active = searchActive,
                        onActiveChange = { searchActive = it }
                    )
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
                    searchActive = !searchActive // Toggle de l'état de la barre de recherche
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
fun SearchExpanded(
    navController: NavController,
    currentDestination: NavDestination?,
    viewModel: MainViewModel
) {
    var searchActive by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(currentDestination) {
        if (currentDestination?.hasRoute<HomeDest>() == true) {
            searchActive = false
            text = ""
        }
    }

    if (searchActive) {
        SearchBarAppli(
            modifier = Modifier
                .width(400.dp)
                .height(190.dp)
                .padding(vertical = 50.dp),
            query = text,
            onQueryChange = { text = it },
            onSearch = { query ->
                searchActive = false
                when {
                    currentDestination?.hasRoute<FilmsDest>() == true -> {
                        viewModel.getFilmsbySearch(query)
                    }
                    currentDestination?.hasRoute<ActeursDest>() == true -> {
                        viewModel.getActeursbySearch(query)
                    }
                    currentDestination?.hasRoute<SeriesDest>() == true -> {
                        viewModel.getSeriesbySearch(query)
                    }
                }
            },
            active = searchActive,
            onActiveChange = { searchActive = it }
        )
    }

    if (currentDestination?.route == "filmDetails/{filmId}"
        || currentDestination?.route == "serieDetails/{serieId}"
        || currentDestination?.route == "acteurDetails/{actorId}"
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ExitToApp,
                contentDescription = "Retour"
            )
        }
    } else {
        FloatingActionButton(
            onClick = {
                searchActive = !searchActive
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Ouvrir la barre de recherche"
            )
        }
    }
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
        NavigationRail {
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