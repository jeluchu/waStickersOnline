package com.jeluchu.wastickersonline.features.details.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.jeluchu.jchucomponents.ktx.coroutines.noCrash
import com.jeluchu.jchucomponents.ktx.strings.getLastBitFromUrl
import com.jeluchu.jchucomponents.ktx.strings.saveImage
import com.jeluchu.wastickersonline.WaStickersOnline
import com.jeluchu.wastickersonline.core.ui.navigation.NavArgs
import com.jeluchu.wastickersonline.core.utils.Enviroments
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.MainActivity
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val item: StickerPack = savedStateHandle[NavArgs.Sticker.key]!!

    var state by mutableStateOf<StickerPack?>(null)
        private set

    init {
        state = item
        getStickerPack()
    }

    private fun getStickerPack() = with(WaStickersOnline.getContext()) {
        noCrash {

            val trayImageFile = item.trayImageFile.getLastBitFromUrl()

            val req = ImageRequest.Builder(this)
                .data(Enviroments.getApiEndpointStickers() + item.identifier + "/" + trayImageFile)
                .target {
                    val myDir =
                        File("${MainActivity.path}/${item.identifier}/try")
                    myDir.mkdirs()
                    val imageName = trayImageFile.replace(".png", "").replace(" ", "_") + ".png"
                    val file = File(myDir, imageName)
                    if (file.exists()) file.delete()
                    try {
                        val out = FileOutputStream(file)
                        it.toBitmap().compress(Bitmap.CompressFormat.PNG, 40, out)
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.build()

            viewModelScope.launch { ImageLoader(this@with).execute(req) }

            for (s in item.stickers) {
                val imageFile = s.imageFile.getLastBitFromUrl()

                val myDir = File("${MainActivity.path}/${item.identifier}")
                myDir.mkdirs()
                val file = File(myDir, imageFile)
                if (file.exists()) file.delete()

                (Enviroments.getApiEndpointStickers() + item.identifier + "/" + imageFile).saveImage(
                    File("${MainActivity.path}/${item.identifier}", imageFile)
                )
            }
        }
    }
}