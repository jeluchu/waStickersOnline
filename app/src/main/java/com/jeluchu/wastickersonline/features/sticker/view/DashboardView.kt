package com.jeluchu.wastickersonline.features.sticker.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jeluchu.jchucomponents.core.states.ListStates
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow
import com.jeluchu.wastickersonline.core.ui.theme.darkGreen
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.milky
import com.jeluchu.wastickersonline.core.ui.theme.primary
import com.jeluchu.wastickersonline.core.ui.theme.secondary
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel

@Composable
fun DashboardView(
    vm: StickersViewModel = hiltViewModel(),
    onStickerClick: (StickerPack) -> Unit
) {
    SystemStatusBarColors(
        statusBarColor = primary,
        systemBarsColor = primary
    )

    val state by vm.state.collectAsStateWithLifecycle()

    DashboardView(
        state = state,
        onStickerClick = onStickerClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardView(
    state: ListStates<StickerPack>,
    act: Activity = LocalContext.current as Activity,
    onStickerClick: (StickerPack) -> Unit
) = Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Stickers") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = primary,
                titleContentColor = darkness
            )
        )
    },
    containerColor = primary
) {
    ScrollableColumn(
        modifier = Modifier.padding(it)
    ) {
        state.data.forEach {
            Column(
                modifier = Modifier.clickable {
                    onStickerClick(it)
                }
            ) {
                Surface(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    shape = 10.cornerRadius(),
                    color = darkGreen,
                    contentColor = milky
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = it.name
                    )
                }

                ListRow(
                    contentPadding = PaddingValues(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(it.stickers) { sticker ->
                        NetworkImage(
                            modifier = Modifier
                                .size(90.dp)
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
