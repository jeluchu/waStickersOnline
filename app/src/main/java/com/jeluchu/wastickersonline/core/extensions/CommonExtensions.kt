package com.jeluchu.wastickersonline.core.extensions

import com.jeluchu.jchucomponents.ktx.strings.empty
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import java.text.Normalizer

fun List<StickerPack>.search(query: String, param: (StickerPack) -> String): List<StickerPack> {
    val sanitizedQuery = normalizeString(query.lowercase())
    val results = mutableListOf<StickerPack>()

    for (item in this) {
        val itemText = param(item)
        val sanitizedItemText = normalizeString(itemText.lowercase())
        if (sanitizedItemText.contains(sanitizedQuery)) results.add(item)
    }

    return results
}

fun normalizeString(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    return normalized.replace(Regex("[^\\p{ASCII}]"), String.empty())
}