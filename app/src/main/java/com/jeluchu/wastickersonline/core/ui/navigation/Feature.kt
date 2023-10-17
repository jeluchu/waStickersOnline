package com.jeluchu.aruppi.core.ui.navigation

import com.jeluchu.wastickersonline.core.ui.navigation.NavItem

enum class Feature(val route: String) {
    DASHBOARD("dashboard"),
    DETAILS("details"),
}

val Feature.baseRoute: String
    get() = NavItem.ContentScreen(this).route

val Feature.nav: NavItem.ContentScreen
    get() = NavItem.ContentScreen(this)
