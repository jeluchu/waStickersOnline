package com.jeluchu.wastickersonline.features.sticker.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsPAndUp
import com.jeluchu.wastickersonline.R

class StickersPreviewDialog(
    val sticker: String
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_sticker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
        }

        val imageLoader = ImageLoader.Builder(requireContext())
            .components {
                if (buildIsPAndUp) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()

        view.findViewById<ImageView>(R.id.ivStickerPreview).load(sticker, imageLoader)
    }
}