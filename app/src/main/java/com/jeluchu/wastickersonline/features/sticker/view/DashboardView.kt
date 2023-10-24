package com.jeluchu.wastickersonline.features.sticker.view

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jeluchu.jchucomponents.core.states.ListStates
import com.jeluchu.jchucomponents.ktx.compose.toImageVector
import com.jeluchu.jchucomponents.ktx.compose.toStringRes
import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButton
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonSettings
import com.jeluchu.jchucomponents.ui.composables.column.ScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.loaders.CircularLoading
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.search
import com.jeluchu.wastickersonline.core.ui.composables.EmptyInfo
import com.jeluchu.wastickersonline.core.ui.composables.SearchTextField
import com.jeluchu.wastickersonline.core.ui.theme.darkGreen
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.milky
import com.jeluchu.wastickersonline.core.ui.theme.primary
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
) {
    val search = rememberMutableStateOf(String.empty())
    val stickers = state.data.search(query = search.value, param = { it.name })
    var isSearchActive by rememberMutableStateOf(false)
    val isVisible = rememberMutableStateOf(true)
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) isVisible.value = false
                if (available.y > 1) isVisible.value = true
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = R.string.app_name.toStringRes(),
                        fontWeight = FontWeight.Bold
                    )
                },
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
                isVisible = isVisible.value,
                floatButton = FloatingButtonSettings(
                    icon = if (isSearchActive) R.drawable.ic_deco_close else R.drawable.ic_deco_search,
                    tint = milky,
                    background = darkGreen
                ),
                onClick = { isSearchActive = !isSearchActive }
            )
        },
        containerColor = primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnimatedVisibility(
                visible = isSearchActive,
                enter = fadeIn() + expandVertically(animationSpec = tween(150, delayMillis = 150)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(150, delayMillis = 150)),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SearchTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 15.dp,
                                end = 15.dp
                            ),
                        value = search.value,
                        onValueChange = { query -> search.value = query },
                        shape = 15.cornerRadius(),
                        singleLine = true,
                        textStyle = TextStyle(color = milky),
                        colors = TextFieldDefaults.colors(
                            cursorColor = milky,
                            focusedTextColor = milky,
                            unfocusedTextColor = milky,
                            disabledTextColor = milky,
                            focusedLabelColor = milky,
                            unfocusedLabelColor = milky,
                            disabledLabelColor = milky,
                            errorLabelColor = milky,
                            errorCursorColor = milky,
                            focusedPlaceholderColor = milky,
                            unfocusedPlaceholderColor = milky,
                            disabledPlaceholderColor = milky,
                            errorPlaceholderColor = milky,
                            focusedSupportingTextColor = milky,
                            unfocusedSupportingTextColor = milky,
                            disabledSupportingTextColor = milky,
                            errorSupportingTextColor = milky,
                            focusedContainerColor = darkness.copy(.6f),
                            unfocusedContainerColor = darkness.copy(.6f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(
                                text = R.string.search.toStringRes(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                tint = milky,
                                modifier = Modifier.size(25.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = String.empty()
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { search.value = String.empty() }
                            ) {
                                Icon(
                                    tint = milky,
                                    modifier = Modifier.size(25.dp),
                                    imageVector = Icons.Default.Close,
                                    contentDescription = String.empty()
                                )
                            }
                        }
                    )
                }
            }

            when {
                state.isLoading -> CircularLoading(isShow = true, colorLoading = darkness)
                stickers.isEmpty() && !state.isLoading -> EmptyInfo()
                else -> {
                    ScrollableColumn {
                        stickers.forEach { pack ->
                            StickerPackItem(
                                stickerPack = pack,
                                onClick = onStickerClick
                            )
                        }
                    }
                }
            }
        }
    }
}
