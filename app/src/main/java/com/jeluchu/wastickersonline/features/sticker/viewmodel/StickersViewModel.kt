package com.jeluchu.wastickersonline.features.sticker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeluchu.jchucomponents.core.failure.handleFailure
import com.jeluchu.jchucomponents.core.states.ListStates
import com.jeluchu.jchucomponents.extensions.flow.flowResourceCollector
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.repository.StickersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StickersViewModel @Inject constructor(
    private val repository: StickersRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ListStates<StickerPack>())
    val state: StateFlow<ListStates<StickerPack>> = _state.asStateFlow()

    init {
        getStickers()
    }

    private fun getStickers() = repository.stickers().flowResourceCollector(
        scope = viewModelScope,
        initialValue = ListStates<StickerPack>(),
        onLoading = { _state.value = ListStates(isLoading = true) },
        onSuccess = { info ->
            _state.value = ListStates(
                data = info.orEmpty(),
                isFloatButtom = info.orEmpty().isNotEmpty()
            )
        },
        onFailure = { failure ->
            _state.value = ListStates(error = failure.handleFailure().orEmpty())
        }
    )
}