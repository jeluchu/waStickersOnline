package com.jeluchu.wastickersonline.features.details.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.lists.ColumnContentAlignment
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyStaticGrid
import com.jeluchu.jchucomponents.ui.foundation.lists.ListColumn
import com.jeluchu.jchucomponents.ui.foundation.lists.RowContentAlignment
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.primary
import com.jeluchu.wastickersonline.core.ui.theme.secondary
import com.jeluchu.wastickersonline.features.details.viewmodel.DetailViewModel
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

@Composable
fun StickersDetailsView(
    vm: DetailViewModel = hiltViewModel(),
    act: Activity = LocalContext.current as Activity
) {
    SystemStatusBarColors(
        statusBarColor = primary,
        systemBarsColor = primary
    )

    StickersDetails(vm.state!!)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StickersDetails(
    stickerPack: StickerPack
) = Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stickerPack.name) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = primary,
                titleContentColor = darkness
            )
        )
    },
    containerColor = primary
) { paddingValues ->
    ListColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(15.dp)
    ) {
        item {
            LazyStaticGrid(
                columns = 4,
                items = stickerPack.stickers,
                rowContentAlignment = RowContentAlignment(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ),
                columnContentAlignment = ColumnContentAlignment(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
            ) { sticker ->
                NetworkImage(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(10.cornerRadius())
                        .background(secondary),
                    url = sticker.imageFile
                )
            }
        }
    }
}