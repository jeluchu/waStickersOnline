package com.jeluchu.wastickersonline.features.sticker.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.jeluchu.aruppi.core.extensions.viewbinding.viewBinding
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.exitActivityLeft
import com.jeluchu.wastickersonline.core.extensions.others.getLastBitFromUrl
import com.jeluchu.wastickersonline.core.extensions.others.noCrash
import com.jeluchu.wastickersonline.core.extensions.others.openInCustomTab
import com.jeluchu.wastickersonline.core.extensions.others.saveImage
import com.jeluchu.wastickersonline.core.extensions.others.simpletext
import com.jeluchu.wastickersonline.core.extensions.others.statusBarColor
import com.jeluchu.wastickersonline.core.utils.ConstantsMeth.Companion.getApiEndpointStickers
import com.jeluchu.wastickersonline.databinding.ActivityStickerDetailsBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_ID
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_NAME
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersDetailsAdapter
import java.io.File
import java.io.FileOutputStream
import java.net.URL
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

        //binding.ivBack.setOnClickListener { exitActivityLeft() }

        //if (intent.extras != null) {
        //    stickerPackView = intent.getSerializableExtra("stickerpack") as StickerPackView?
       // }

       // adapterStickers.collection = stickerPackView?.stickers.orEmpty()

        //path = filesDir.toString() + "/" + "stickers_asset" + "/" + stickerPackView!!.identifier + "/"

        //binding.ivTrayImage.load(stickerPackView!!.trayImageFile)
        //binding.tvPackName.simpletext(stickerPackView!!.name)
        //binding.tvAuthor.simpletext(stickerPackView!!.publisher)


        //binding.rvStickers.adapter = adapterStickers
        //binding.rvStickers.apply {
        //    setHasFixedSize(true)
        //    setItemViewCacheSize(30)
        //}
        //binding.rvStickers.scheduleLayoutAnimation()
/*
        binding.mcvAddToWhatsApp.setOnClickListener {
            val intent = Intent()
            intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            intent.putExtra(EXTRA_STICKER_PACK_ID, stickerPackView!!.identifier.toString())
            intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
            intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackView!!.name)
            try {
                startActivityForResult(intent, ADD_PACK)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this@StickerDetailsActivity, "No se añadió el paquete de stickers. Si deseas añadirlo, instala o actualiza WhatsApp.", Toast.LENGTH_LONG).show()
            }
        }
        binding.mcvAddToTelegram.setOnClickListener { openInCustomTab(stickerPackView!!.publisherWebsite) }
*/
    }

    private fun initUI() = with(binding) {
        if (intent.extras != null) {
            stickerPackView = intent.getSerializableExtra("stickerpack") as StickerPackView?
        }

        ivTrayImage.load(stickerPackView!!.trayImageFile)
        tvPackName.simpletext(stickerPackView!!.name)
        tvAuthor.simpletext(stickerPackView!!.publisher)

        adapterStickers.collection = stickerPackView?.stickers.orEmpty()
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

    override fun onBackPressed() {
        super.onBackPressed()
        exitActivityLeft()
    }

    companion object {
        private const val ADD_PACK = 200
        var path: String? = null
    }
}