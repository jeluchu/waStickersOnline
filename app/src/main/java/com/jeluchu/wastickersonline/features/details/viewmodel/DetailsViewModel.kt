package com.jeluchu.wastickersonline.features.details.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jeluchu.wastickersonline.core.ui.navigation.NavArgs
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val item: StickerPack = savedStateHandle[NavArgs.Sticker.key]!!

    var state by mutableStateOf<StickerPack?>(null)
        private set

    init {
        state = item
    }
}