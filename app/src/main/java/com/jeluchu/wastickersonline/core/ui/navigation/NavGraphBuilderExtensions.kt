package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavGraphBuilder
import com.jeluchu.aruppi.core.ui.navigation.Destinations
import com.jeluchu.aruppi.core.ui.navigation.Feature
import com.jeluchu.aruppi.core.ui.navigation.nav
import com.jeluchu.wastickersonline.features.sticker.view.DashboardView
import com.jeluchu.wastickersonline.features.sticker.view.StickersDetailsView

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.mainNav(nav: Destinations) {
    composable(Feature.DASHBOARD.nav) {
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
    composable(Feature.DETAILS.nav) {
        StickersDetailsView(
            //onBackClick = { nav.goBack(it) },
            //onItemClick = {}
            //onSearchClick = { /*openActivityFromRight(SearchActivity::class.java)*/  },
            //onCategoryClick = { /*showDialogFragment(CategoryDialogFragment())*/ }
        )
    }
}