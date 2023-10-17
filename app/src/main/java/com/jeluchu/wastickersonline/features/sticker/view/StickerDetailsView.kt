package com.jeluchu.wastickersonline.features.sticker.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow
import com.jeluchu.wastickersonline.core.extensions.others.openActivity
import com.jeluchu.wastickersonline.core.extensions.others.openActivityRight
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.primary
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKERPACK
import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel

@Composable
fun StickersDetailsView(

    act: Activity = LocalContext.current as Activity
) {
    SystemStatusBarColors(
        statusBarColor = primary,
        systemBarsColor = primary
    )

    StickersDetails()

    NavType

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StickersDetails(

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

}