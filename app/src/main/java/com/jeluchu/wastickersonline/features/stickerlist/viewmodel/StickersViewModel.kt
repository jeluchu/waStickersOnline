package com.jeluchu.wastickersonline.features.stickerlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeluchu.wastickersonline.core.extensions.job.cancelIfActive
import com.jeluchu.wastickersonline.core.functional.map
import com.jeluchu.wastickersonline.core.interactor.UseCase
import com.jeluchu.wastickersonline.core.platform.BaseViewModel
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.features.stickerlist.usecase.GetStickers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StickersViewModel(private val getStickers: GetStickers) : BaseViewModel() {

    var sticker = MutableLiveData<List<StickerPackView>>()

    var getStickersJob: Job? = null

    fun getStickers() {
        getStickersJob.cancelIfActive()
        getStickersJob = viewModelScope.launch {
            getStickers(UseCase.None())
                .onStart { handleShowSpinner(true) }
                .onEach { handleShowSpinner(false) }
                .map { either ->
                    either.map { list ->
                        list.map { article ->
                            article.toStickersPackView()
                        }
                    }
                }.collect { it.fold(::handleFailure, ::handleStickersList) }
        }
    }
    private fun handleStickersList(stickers: List<StickerPackView>) {
        this.sticker.value = stickers
    }

}