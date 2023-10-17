package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@ExperimentalAnimationApi
val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
    slideInHorizontally(
        animationSpec = tween(250),
        initialOffsetX = { fullWidth -> fullWidth }
    )
}

@ExperimentalAnimationApi
val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
    slideOutHorizontally(
        animationSpec = tween(250),
        targetOffsetX = { fullWidth -> fullWidth }
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composable(
    navItem: Screen,
    arguments: List<NamedNavArgument> = emptyList(),
    enterAnimation: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = enterTransition,
    exitAnimation: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = exitTransition,
    popEnterAnimation: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = enterTransition,
    popExitAnimation: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = exitTransition,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = arguments,
        enterTransition = enterAnimation,
        exitTransition = exitAnimation,
        popEnterTransition = popEnterAnimation,
        popExitTransition = popExitAnimation
    ) { content(it) }
}
