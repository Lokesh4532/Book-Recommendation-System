package com.example.bookapp.common.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookapp.R


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {
    object Home: BottomBarScreen(
        route = "addedBook",
        title = "Home",
        icon = R.drawable.fav,
        icon_focused = R.drawable.fav
    )

    object Add: BottomBarScreen(
        route = "addBookScreen",
        title = "Liked",
        icon = R.drawable.add,
        icon_focused = R.drawable.add
    )
    object Liked:BottomBarScreen(
        route = "aiScreen",
        title = "Add",
        icon = R.drawable.book,
        icon_focused = R.drawable.book
    )
    object Bot:BottomBarScreen(
        route = "chat",
        title = "Add",
        icon = R.drawable.bot,
        icon_focused = R.drawable.bot
    )
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Add,
        BottomBarScreen.Liked,
        BottomBarScreen.Bot
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.45f))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    // Check if the current screen is selected
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    // Define color based on the selection state
    val contentColor = if (selected) Color(0xFFFF2400) else Color.White

    // Box for each item
    Box(
        modifier = Modifier
            .height(50.dp)
            .clip(CircleShape)
            .clickable{
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
    ) {
        // Icon positioned at the bottom center of the Box
        Icon(
            painter = painterResource(if (selected) screen.icon_focused else screen.icon),
            contentDescription = screen.title,
            tint = contentColor,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center) // Correctly position at the bottom center
        )
    }
}
