package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navArgument
import com.jeluchu.wastickersonline.features.details.view.StickersDetailsView
import com.jeluchu.wastickersonline.features.sticker.view.DashboardView
import com.jeluchu.wastickersonline.features.sticker.view.StickerPackType

fun NavGraphBuilder.mainNav(nav: Destinations) {
    composable(Screen.Dashboard) {
        DashboardView(
            onStickerClick = { sticker ->
                nav.goToDetails(sticker)
                /*

                                   act.openActivity(StickerDetailsActivity::class.java) {
                        putParcelable(EXTRA_STICKERPACK, it)
                    }
                    act.openActivityRight()

                 */
            }
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