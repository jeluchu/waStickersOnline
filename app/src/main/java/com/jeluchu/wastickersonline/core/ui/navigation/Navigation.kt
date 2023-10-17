package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.jeluchu.aruppi.core.ui.navigation.Feature

@Composable
fun Navigation() {
    ProvideNavHostController { navHost ->
        ProvideNavigate { nav ->
            NavHost(
                navController = navHost,
                startDestination = Feature.DASHBOARD.route
            ) {
                mainNav(nav)
                detailsNav(nav)
            }
        }
    }
}