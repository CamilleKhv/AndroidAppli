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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
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

@Serializable
class ExamDest


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

                    WindowWidthSizeClass.MEDIUM ->
                        CompactTopBar(navController, currentDestination, viewModel)

                    else -> {}
                }
            }
        },
        floatingActionButton = {
            if (classWidth != WindowWidthSizeClass.COMPACT
                && classWidth != WindowWidthSizeClass.MEDIUM
                && currentDestination?.hasRoute<HomeDest>() != true
            ) {
                SearchExpanded(navController, currentDestination, viewModel)
            }
        },
        bottomBar = {
            if (currentDestination?.hasRoute<HomeDest>() != true) {
                when (classWidth) {
                    WindowWidthSizeClass.COMPACT -> BottomBar(navController, currentDestination)
                    WindowWidthSizeClass.MEDIUM -> BottomBar(navController, currentDestination)
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
            if (classWidth != WindowWidthSizeClass.COMPACT
                && classWidth != WindowWidthSizeClass.MEDIUM
                && currentDestination?.hasRoute<HomeDest>() != true
            ) {
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
                    composable<ExamDest> { ScreenExam() }

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
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color(250, 140, 140, 255)
        )
    ) {}
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(255, 88, 88, 255)
        ),
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
            modifier = Modifier.padding(16.dp),
            containerColor = Color(255, 88, 88, 255)
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
            modifier = Modifier.padding(16.dp),
            containerColor = Color(255, 88, 88, 255)
        ) {
            Icon(
                imageVector = if (searchActive) Icons.Rounded.ArrowBack else Icons.Rounded.Search,
                contentDescription = if (searchActive) "Fermer la barre de recherche" else "Ouvrir la barre de recherche"
            )
        }
    }
}


@Composable
fun BottomBar(navController: NavController, currentDestination: NavDestination?) {
    NavigationBar(containerColor = Color(255, 88, 88, 255)) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.operateur_camera),
                    contentDescription = "Logo Films",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Films", fontSize = 16.sp) },
            selected = currentDestination?.hasRoute<FilmsDest>() == true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(196, 71, 71, 255)
            ),
            onClick = { navController.navigate(FilmsDest()) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.smart),
                    contentDescription = "Logo series",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Séries", fontSize = 16.sp) },
            selected = currentDestination?.hasRoute<SeriesDest>() == true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(196, 71, 71, 255)
            ),
            onClick = { navController.navigate(SeriesDest()) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.personne_debout),
                    contentDescription = "Logo acteurs",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Acteurs", fontSize = 16.sp) },
            selected = currentDestination?.hasRoute<ActeursDest>() == true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(196, 71, 71, 255)
            ),
            onClick = { navController.navigate(ActeursDest()) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(R.drawable.collection),
                    contentDescription = "Logo Collection",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Collections", fontSize = 16.sp) },
            selected = currentDestination?.hasRoute<ExamDest>() == true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(196, 71, 71, 255)
            ),
            onClick = { navController.navigate(ExamDest()) }
        )
    }
}

@Composable
fun SideBar(navController: NavController, currentDestination: NavDestination?) {
    Column(modifier = Modifier.fillMaxHeight()) {
        NavigationRail(containerColor = Color(255, 88, 88, 255)) {
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.operateur_camera),
                        contentDescription = "Logo Films",
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(text = "Films", fontSize = 14.sp) },
                selected = currentDestination?.hasRoute<FilmsDest>() == true,
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = Color(196, 71, 71, 255)
                ),
                onClick = { navController.navigate(FilmsDest()) }
            )
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.smart),
                        contentDescription = "Logo series",
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(text = "Séries", fontSize = 14.sp) },
                selected = currentDestination?.hasRoute<SeriesDest>() == true,
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = Color(196, 71, 71, 255)
                ),
                onClick = { navController.navigate(SeriesDest()) }
            )
            NavigationRailItem(
                modifier = Modifier.weight(1f),
                icon = {
                    Image(
                        painter = painterResource(R.drawable.personne_debout),
                        contentDescription = "Logo acteurs",
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(text = "Acteurs", fontSize = 14.sp) },
                selected = currentDestination?.hasRoute<ActeursDest>() == true,
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = Color(196, 71, 71, 255)
                ),
                onClick = { navController.navigate(ActeursDest()) }
            )
            NavigationRail(containerColor = Color(255, 88, 88, 255)) {
                NavigationRailItem(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Image(
                            painter = painterResource(R.drawable.collection),
                            contentDescription = "Logo Collection",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    label = { Text(text = "Collections", fontSize = 14.sp) },
                    selected = currentDestination?.hasRoute<ExamDest>() == true,
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = Color(196, 71, 71, 255)
                    ),
                    onClick = { navController.navigate(ExamDest()) }
                )
            }
        }
    }
}