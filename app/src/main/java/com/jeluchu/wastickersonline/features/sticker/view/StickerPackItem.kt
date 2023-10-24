package com.jeluchu.wastickersonline.features.sticker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.animations.lists.Animations
import com.jeluchu.jchucomponents.ui.animations.lists.animateItem
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.extensions.modifier.noRippleClickable
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow
import com.jeluchu.wastickersonline.core.ui.theme.darkGreen
import com.jeluchu.wastickersonline.core.ui.theme.darkness
import com.jeluchu.wastickersonline.core.ui.theme.secondary
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

@Composable
fun StickerPackItem(
    stickerPack: StickerPack,
    onClick: (StickerPack) -> Unit
) = Column(
    modifier = Modifier.noRippleClickable { onClick(stickerPack) }
) {
    Surface(
        modifier = Modifier.padding(horizontal = 15.dp),
        shape = 10.cornerRadius(),
        color = darkGreen.copy(.2f),
        contentColor = darkness.copy(.7f)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = stickerPack.name,
            fontWeight = FontWeight.Bold
        )
    }

    ListRow(
        contentPadding = PaddingValues(15.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(stickerPack.stickers, key = { it }) { sticker ->
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