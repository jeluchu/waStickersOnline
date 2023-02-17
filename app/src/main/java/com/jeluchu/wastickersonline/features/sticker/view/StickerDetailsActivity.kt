package com.jeluchu.wastickersonline.features.sticker.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.jeluchu.jchucomponents.ktx.coroutines.noCrash
import com.jeluchu.jchucomponents.ktx.strings.getLastBitFromUrl
import com.jeluchu.jchucomponents.ktx.strings.saveImage
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.core.extensions.others.exitActivityLeft
import com.jeluchu.wastickersonline.core.extensions.others.openInCustomTab
import com.jeluchu.wastickersonline.core.extensions.others.simpleText
import com.jeluchu.wastickersonline.core.extensions.others.statusBarColor
import com.jeluchu.wastickersonline.core.extensions.serializable
import com.jeluchu.wastickersonline.core.extensions.viewbinding.viewBinding
import com.jeluchu.wastickersonline.core.utils.ConstantsMeth.Companion.getApiEndpointStickers
import com.jeluchu.wastickersonline.databinding.ActivityStickerDetailsBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_ID
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_NAME
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersDetailsAdapter
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StickerDetailsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityStickerDetailsBinding::inflate)

    private val adapterStickers: StickersDetailsAdapter by inject()

    private var stickerPackView: StickerPackView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        statusBarColor()
        initUI()
        initListeners()
        getStickerPack()
    }

    private fun initUI() = with(binding) {
        if (intent.extras != null) {
            stickerPackView = intent.serializable("stickerpack") as StickerPackView?
        }

        ivTrayImage.load(stickerPackView!!.trayImageFile)
        tvPackName.simpleText(stickerPackView!!.name)
        tvAuthor.simpleText(stickerPackView!!.publisher)

        adapterStickers.supportFragmentManager = supportFragmentManager
        adapterStickers.submitList(stickerPackView?.stickers.orEmpty())
        rvStickers.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(30)
            adapter = adapterStickers
            scheduleLayoutAnimation()
        }

    }

    private fun initListeners() = with(binding) {
        ivBack.setOnClickListener { exitActivityLeft() }
        mcvAddToWhatsApp.setOnClickListener {
            val intent = Intent().apply {
                action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
                putExtra(EXTRA_STICKER_PACK_ID, stickerPackView!!.identifier.toString())
                putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
                putExtra(EXTRA_STICKER_PACK_NAME, stickerPackView!!.name)
            }
            try {
                startActivityForResult(intent, 200)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@StickerDetailsActivity,
                    "No se añadió el paquete de stickers. Si deseas añadirlo, instala o actualiza WhatsApp.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        mcvAddToTelegram.setOnClickListener { openInCustomTab(stickerPackView!!.publisherWebsite) }
        onBackPressedDispatcher.addCallback(
            this@StickerDetailsActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = exitActivityLeft()
            }
        )
    }

    private fun getStickerPack() {
        noCrash {

            val trayImageFile = stickerPackView!!.trayImageFile.getLastBitFromUrl()

            val req = ImageRequest.Builder(this@StickerDetailsActivity)
                .data(getApiEndpointStickers() + stickerPackView!!.identifier + "/" + trayImageFile)
                .target {
                    val myDir =
                        File("${MainActivity.path}/${stickerPackView!!.identifier}/try")
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

            lifecycleScope.launch { ImageLoader(this@StickerDetailsActivity).execute(req) }

            for (s in stickerPackView!!.stickers) {
                val imageFile = s.imageFile.getLastBitFromUrl()

                val myDir = File("${MainActivity.path}/${stickerPackView!!.identifier}")
                myDir.mkdirs()
                val file = File(myDir, imageFile)
                if (file.exists()) file.delete()

                (getApiEndpointStickers() + stickerPackView!!.identifier + "/" + imageFile).saveImage(
                    File("${MainActivity.path}/${stickerPackView!!.identifier}", imageFile)
                )
            }
        }
    }
}