package com.example.myapplication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController, actName: String, ourColumn: @Composable (PaddingValues) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var showDialog by remember { mutableStateOf(false) }
    val logoPainter: Painter = painterResource(id = R.drawable.newbanditlogo)

    if (showDialog) {
        DialogBox(
            showDialog = showDialog,
            onDismissRequest = { showDialog = false },
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(navController, logoPainter, scrollBehavior) { showDialog = true } },
        bottomBar = {BottomNavigationBar(navController)},
        content = ourColumn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, logo: Painter, scrollBehavior: TopAppBarScrollBehavior, onInfoClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .padding(0.dp, 10.dp, 0.dp, 0.dp),
                painter = logo,
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onInfoClicked) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("search") }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home")},
                label = { Text(text = "Home") },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == "home",
                onClick = {
                    navController.navigate("home") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "personal")},
                label = { Text(text = "My Page") },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == "personal",
                onClick = {
                    navController.navigate("personal") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

// Placeholder for AlertDialogExample Composable
@Composable
fun DialogBox(
    showDialog: Boolean,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        AlertDialog( modifier = Modifier
            .background(MaterialTheme.colorScheme.background) ,
            icon = {
                Icon(Icons.Filled.Info, contentDescription = "Info Icon", tint = MaterialTheme.colorScheme.onBackground)
            },
            title = {
                Text(text = "BoardGame Bandits", color = MaterialTheme.colorScheme.onBackground)
            },
            text = {
                Text(text = "Is an app developed in Kotlin for Android. Its a platform for " +
                        "board game enthusiasts. It features functionalities for exploring " +
                        "various board games, providing users with detailed information about " +
                        "each game. Users can browse different categories of board games, view " +
                        "specific details, and possibly interact with some aspects related to " +
                        "board gaming. The app's design caters to those interested in discovering " +
                        "and learning more about board games, enhancing their gaming experience " +
                        "with accessible information and user-friendly navigation.", color = MaterialTheme.colorScheme.onBackground)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Close", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}