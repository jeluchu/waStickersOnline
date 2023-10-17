package com.jeluchu.wastickersonline.features.details.view

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.jeluchu.jchucomponents.ktx.coroutines.noCrash
import com.jeluchu.jchucomponents.ktx.strings.getLastBitFromUrl
import com.jeluchu.jchucomponents.ktx.strings.saveImage
import com.jeluchu.wastickersonline.core.utils.ConstantsMeth.Companion.getApiEndpointStickers
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class StickerDetailsActivity : AppCompatActivity() {

    //private val binding by viewBinding(ActivityStickerDetailsBinding::inflate)

    //private val adapterStickers: StickersDetailsAdapter by inject()

    private var stickerPackView: StickerPack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)
        //initUI()
        //initListeners()
        //if (intent.extras != null) {
        //    stickerPackView = intent.serializable("stickerpack") as StickerPack?
        //}

        getStickerPack()
        setContent {
            StickersDetailsView()
        }
    }

    /*
        private fun initUI() = with(binding) {
            if (intent.extras != null) {
                stickerPackView = intent.serializable("stickerpack") as StickerPack?
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
    */
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