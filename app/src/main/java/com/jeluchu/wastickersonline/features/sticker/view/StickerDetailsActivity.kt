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
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersDetailsAdapter
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.*
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_ID
import com.jeluchu.wastickersonline.features.sticker.view.MainActivity.Companion.EXTRA_STICKER_PACK_NAME
import kotlinx.android.synthetic.main.activity_sticker_details.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class StickerDetailsActivity : AppCompatActivity() {

    private var stickerPackView: StickerPackView? = null

    private val adapterStickers: StickersDetailsAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_details)

        statusBarColor()

        ivBack.setOnClickListener { exitActivityLeft() }

        if (intent.extras != null) {
            stickerPackView = intent.getSerializableExtra("stickerpack") as StickerPackView?
        }

        adapterStickers.collection = stickerPackView?.stickers.orEmpty()

        path = filesDir.toString() + "/" + "stickers_asset" + "/" + stickerPackView!!.identifier + "/"

        ivTrayImage.load(stickerPackView!!.trayImageFile)
        tvPackName.simpletext(stickerPackView!!.name)
        tvAuthor.simpletext(stickerPackView!!.publisher)

        getStickerPack()

        rvStickers.adapter = adapterStickers
        rvStickers.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(30)
        }
        rvStickers.scheduleLayoutAnimation()

        mcvAddToWhatsApp.setOnClickListener {
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
        mcvAddToTelegram.setOnClickListener { openInCustomTab(stickerPackView!!.publisherWebsite) }

    }

    private fun getStickerPack() {
        noCrash {
            val trayImageFile = getLastBitFromUrl(stickerPackView!!.trayImageFile)

            val loader = ImageLoader(this@StickerDetailsActivity)
            val req = ImageRequest.Builder(this@StickerDetailsActivity)
                    .data("https://aruppi.jeluchu.xyz/res/stickers/" + stickerPackView!!.identifier + "/" + trayImageFile)
                    .target {
                        val myDir = File("${MainActivity.path}/${stickerPackView!!.identifier}/try")
                        myDir.mkdirs()
                        val fname = trayImageFile.replace(".png", "").replace(" ", "_") + ".png"
                        val file = File(myDir, fname)
                        if (file.exists()) file.delete()
                        try {
                            val out = FileOutputStream(file)
                            it.toBitmap().compress(Bitmap.CompressFormat.PNG, 40, out)
                            out.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }.build()
            lifecycleScope.launch { loader.execute(req) }

            for (s in stickerPackView!!.stickers) {
                val imageFile = getLastBitFromUrl(s.imageFile)

                val myDir = File("${MainActivity.path}/${stickerPackView!!.identifier}")
                myDir.mkdirs()
                val file = File(myDir, imageFile)
                if (file.exists()) file.delete()

                saveImage(
                        "https://aruppi.jeluchu.xyz/res/stickers/" + stickerPackView!!.identifier + "/" + imageFile,
                        File("${MainActivity.path}/${stickerPackView!!.identifier}", imageFile)
                )
            }
        }
    }

    private fun saveImage(imageUrl: String, destinationFile: File) {
        try {
            Thread {
                val url = URL(imageUrl)
                val inputStream = url.openStream()
                val os = FileOutputStream(destinationFile)
                val b = ByteArray(2048)
                var length: Int
                while (inputStream.read(b).also { length = it } != -1) {
                    os.write(b, 0, length)
                }
                inputStream?.close()
                os.close()
            }.start()

        } catch (e: Exception) {
            e.printStackTrace()
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