package com.jeluchu.wastickersonline.features.stickerlist.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.features.stickerlist.view.MainActivity.Companion.EXTRA_STICKER_PACK_AUTHORITY
import com.jeluchu.wastickersonline.features.stickerlist.view.MainActivity.Companion.EXTRA_STICKER_PACK_ID
import com.jeluchu.wastickersonline.features.stickerlist.view.MainActivity.Companion.EXTRA_STICKER_PACK_NAME
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.features.stickerlist.adapter.StickersDetailsAdapter
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerView
import kotlinx.android.synthetic.main.activity_sticker_details.*
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*

class StickerDetailsActivity : AppCompatActivity() {

    var stickers: List<StickerView>? = null
    var strings: ArrayList<String>? = null
    var stickerPackView: StickerPackView? = null

    private val adapterStickers: StickersDetailsAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_details)

        if (intent.extras != null) {
            stickerPackView = intent.getSerializableExtra("stickerpack") as StickerPackView?
        }

        adapterStickers.collection = stickerPackView?.stickers.orEmpty()

        stickers = stickerPackView!!.stickers
        strings = ArrayList()
        path = filesDir.toString() + "/" + "stickers_asset" + "/" + stickerPackView!!.identifier + "/"
        val name = getLastBitFromUrl(stickerPackView!!.stickers[0].imageFile).replace(" ", "_")
        val file = File(path + name)
        for ((_, imageFile) in stickers!!) {
            if (!file.exists()) {
                strings!!.add(getLastBitFromUrl(imageFile))
            } else {
                strings!!.add(path + getLastBitFromUrl(imageFile))
            }
        }

        runOnUiThread {

            val trayImageFile = getLastBitFromUrl(stickerPackView!!.trayImageFile)

            Glide.with(this)
                    .asBitmap()
                    .load("https://aruppi.jeluchu.xyz/res/stickers/" + stickerPackView!!.identifier + "/" + trayImageFile)
                    .addListener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            val bitmap1 = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888)
                            val matrix = Matrix()
                            val canvas = Canvas(bitmap1)
                            canvas.drawColor(Color.TRANSPARENT)
                            matrix.postTranslate(
                                    canvas.width / 2 - resource.width / 2.toFloat(),
                                    canvas.height / 2 - resource.height / 2
                                            .toFloat())
                            canvas.drawBitmap(resource, matrix, null)
                            MainActivity.saveTryImage(bitmap1, trayImageFile, stickerPackView!!.identifier.toString())
                            return true
                        }
                    }).submit()

            for (s in stickerPackView!!.stickers) {

                val imageFile = getLastBitFromUrl(s.imageFile)

                Glide.with(this)
                        .asBitmap()
                        .apply(RequestOptions().override(512, 512))
                        .load("https://aruppi.jeluchu.xyz/res/stickers/" + stickerPackView!!.identifier + "/" + imageFile)
                        .addListener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                val bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
                                val matrix = Matrix()
                                val canvas = Canvas(bitmap1)
                                canvas.drawColor(Color.TRANSPARENT)
                                matrix.postTranslate(
                                        canvas.width / 2 - resource.width / 2.toFloat(),
                                        canvas.height / 2 - resource.height / 2
                                                .toFloat())
                                canvas.drawBitmap(resource, matrix, null)
                                MainActivity.saveImage(bitmap1, imageFile, stickerPackView!!.identifier)
                                return true
                            }
                        }).submit()
            }





        }

        //adapter = StickesDetailsAdapter(stickers!!, this)
        val gridLayoutManager = GridLayoutManager(this, 4)
        rvStickersPack.layoutManager = gridLayoutManager
        rvStickersPack.adapter = adapterStickers

        bAddWhatsApp.setOnClickListener {
            val intent = Intent()
            intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            intent.putExtra(EXTRA_STICKER_PACK_ID, stickerPackView!!.identifier.toString())
            intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
            intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackView!!.name)
            try {
                startActivityForResult(intent, ADD_PACK)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this@StickerDetailsActivity, "error", Toast.LENGTH_LONG).show()
            }
        }

    }

    companion object {
        private const val ADD_PACK = 200
        var path: String? = null
        private fun getLastBitFromUrl(url: String): String {
            return url.replaceFirst(".*/([^/?]+).*".toRegex(), "$1")
        }
    }
}