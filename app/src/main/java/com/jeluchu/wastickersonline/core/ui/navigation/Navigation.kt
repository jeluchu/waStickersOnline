package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost

@Composable
fun Navigation() {
    ProvideNavHostController { navHost ->
        ProvideNavigate { nav ->
            NavHost(
                navController = navHost,
                startDestination = Screen.Dashboard.route
            ) {
                mainNav(nav)
                detailsNav(nav)
            }
        }
    }
}