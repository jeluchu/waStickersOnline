package com.jeluchu.wastickersonline.features.sticker.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jeluchu.jchucomponents.core.states.ListStates
import com.jeluchu.jchucomponents.ktx.compose.toImageVector
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.animations.lists.Animations
import com.jeluchu.jchucomponents.ui.animations.lists.animateItem
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButton
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonSettings
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.extensions.modifier.noRippleClickable
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.ui.theme.darkGreen
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.milky
import com.jeluchu.wastickersonline.core.ui.theme.primary
import com.jeluchu.wastickersonline.core.ui.theme.secondary
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel

@Composable
fun DashboardView(
    ctx: Context = LocalContext.current,
    uri: UriHandler = LocalUriHandler.current,
    vm: StickersViewModel = hiltViewModel(),
    onStickerClick: (StickerPack) -> Unit,
    onLogoClick: (UriHandler) -> Unit,
    onRateClick: (Context) -> Unit
) {
    SystemStatusBarColors(
        statusBarColor = primary,
        systemBarsColor = primary
    )

    val state by vm.state.collectAsStateWithLifecycle()

    DashboardView(
        state = state,
        onStickerClick = onStickerClick,
        onLogoClick = { onLogoClick(uri) },
        onRateClick = { onRateClick(ctx) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardView(
    state: ListStates<StickerPack>,
    onStickerClick: (StickerPack) -> Unit,
    onLogoClick: () -> Unit,
    onRateClick: () -> Unit,
) = Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Stickers") },
            navigationIcon = {
                IconLink(
                    tint = darkness,
                    modifier = Modifier.size(30.dp),
                    imageVector = R.drawable.ic_deco_sticker.toImageVector(),
                    contentDescription = "",
                    onClick = onLogoClick
                )
            },
            actions = {
                IconLink(
                    tint = darkness,
                    modifier = Modifier.size(30.dp),
                    imageVector = R.drawable.ic_deco_rate_app.toImageVector(),
                    contentDescription = "",
                    onClick = onRateClick
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = primary,
                titleContentColor = darkness
            )
        )
    },
    floatingActionButton = {
        FloatingButton(
            floatButton = FloatingButtonSettings(
                icon = R.drawable.ic_deco_search,
                tint = milky,
                background = darkGreen
            ),
            onClick = { /*isSearchActive = !isSearchActive*/ }
        )
    },
    containerColor = primary
) { paddingValues ->
    ScrollableColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        state.data.forEach {
            Column(
                modifier = Modifier.noRippleClickable { onStickerClick(it) }
            ) {
                Surface(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    shape = 10.cornerRadius(),
                    color = darkGreen.copy(.2f),
                    contentColor = darkness.copy(.7f)
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = it.name,
                        fontWeight = FontWeight.Bold
                    )
                }

                ListRow(
                    contentPadding = PaddingValues(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(it.stickers, key = { it }) { sticker ->
                        NetworkImage(
                            modifier = Modifier
                                .size(90.dp)
                                .animateItem(Animations.Scale)
                                .clip(10.cornerRadius())
                                .background(secondary),
                            url = sticker.imageFile,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}
