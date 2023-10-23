package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
import com.jeluchu.wastickersonline.core.utils.Links
import com.jeluchu.wastickersonline.features.details.view.StickersDetailsView
import com.jeluchu.wastickersonline.features.sticker.view.DashboardView

fun NavGraphBuilder.mainNav(nav: Destinations) {
    composable(Screen.Dashboard) {
        DashboardView(
            onStickerClick = { sticker -> nav.goToDetails(sticker) },
            onLogoClick = { uri -> uri.openUri(Links.website) }
        )
    }
}

fun NavGraphBuilder.detailsNav(nav: Destinations) {
    composable(
        navItem = Screen.Detail,
        arguments = listOf(navArgument(NavArgs.Sticker.key) { type = StickerPackType })
    ) {
        StickersDetailsView(
            onBackClick = { nav.goBack(it) },
            //onItemClick = {}
            //onSearchClick = { /*openActivityFromRight(SearchActivity::class.java)*/  },
            //onCategoryClick = { /*showDialogFragment(CategoryDialogFragment())*/ }
        )
    }
}