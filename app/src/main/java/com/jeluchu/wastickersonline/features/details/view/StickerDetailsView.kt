package com.jeluchu.wastickersonline.features.details.view

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.jeluchu.jchucomponents.ktx.compose.toImageVector
import com.jeluchu.jchucomponents.ktx.compose.toStringRes
import com.jeluchu.jchucomponents.ktx.numbers.height
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsPAndUp
import com.jeluchu.jchucomponents.ui.accompanist.systemui.SystemStatusBarColors
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink
import com.jeluchu.jchucomponents.ui.foundation.lists.ColumnContentAlignment
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyStaticGrid
import com.jeluchu.jchucomponents.ui.foundation.lists.ListColumn
import com.jeluchu.jchucomponents.ui.foundation.lists.RowContentAlignment
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.addStickers
import com.jeluchu.wastickersonline.core.ui.theme.darkGreen
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.primary
import com.jeluchu.wastickersonline.core.ui.theme.secondary
import com.jeluchu.wastickersonline.features.details.viewmodel.DetailViewModel
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

@Composable
fun StickersDetailsView(
    onBackClick: () -> Unit,
    vm: DetailViewModel = hiltViewModel(),
    act: Activity = LocalContext.current as Activity
) {
    SystemStatusBarColors(
        statusBarColor = primary,
        systemBarsColor = darkGreen
    )

    StickersDetails(
        activity = act,
        onBackClick = onBackClick,
        stickerPack = vm.state!!,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StickersDetails(
    activity: Activity,
    onBackClick: () -> Unit,
    stickerPack: StickerPack
) = Box {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stickerPack.name) },
                navigationIcon = {
                    IconLink(
                        imageVector = R.drawable.ic_arrow_left.toImageVector(),
                        onClick = onBackClick,
                        contentDescription = ""
                    )
                },
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
                    val context = LocalContext.current
                    val imageLoader = ImageLoader.Builder(context)
                        .components {
                            if (buildIsPAndUp) add(ImageDecoderDecoder.Factory())
                            else add(GifDecoder.Factory())
                        }
                        .build()

                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(data = sticker.imageFile)
                                .error(R.drawable.sticker_error).apply(block = {
                                    size(Size.ORIGINAL)
                                }).build(), imageLoader = imageLoader
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(10.cornerRadius())
                            .background(secondary)
                    )
                }
            }
            item { 100.height() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(darkGreen)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = 15.cornerRadius(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary
            ),
            onClick = { activity.addStickers(stickerPack) },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = R.string.add_to_whatsapp.toStringRes(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = darkness
            )
        }
    }
}