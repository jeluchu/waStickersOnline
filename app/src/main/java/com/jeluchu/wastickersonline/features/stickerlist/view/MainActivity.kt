package com.jeluchu.wastickersonline.features.stickerlist.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.observe
import com.jeluchu.wastickersonline.features.stickerlist.adapter.StickersAdapter
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.features.stickerlist.viewmodel.StickersViewModel
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.io.*

class MainActivity : AppCompatActivity() {

    private val permissionsList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private val getStickersView: StickersViewModel by inject()
    private val adapterStickers: StickersAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(getStickersView) {
            observe(sticker, ::renderStickersList)
            failure(failure, ::handleFailure)
        }

        loadStickers()
        initListeners()

        path = "$filesDir/stickers_asset"

        permissions
        setContentView(R.layout.activity_main)
        rvStickersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvStickersList.adapter = adapterStickers
    }

    private fun initListeners() {
        adapterStickers.clickListener = {
            val intent = Intent(this, StickerDetailsActivity::class.java)
            intent.putExtra(EXTRA_STICKERPACK, it)
            startActivity(intent)
        }
    }

    private fun loadStickers() = getStickersView.getStickers()

    private fun renderStickersList(stickersView: List<StickerPackView>?) {
        val stickerPack : ArrayList<StickerPackView> = stickersView as ArrayList<StickerPackView>
        Hawk.put<ArrayList<StickerPackView>>("sticker_packs", stickerPack)

        adapterStickers.collection = stickersView.orEmpty()
    }
    private fun handleFailure(failure: Failure?) { failure.toString() }

    private val permissions: Unit
        get() {
            val perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (perm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        permissionsList,
                        1
                )
            }
        }

    companion object {
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val EXTRA_STICKERPACK = "stickerpack"

        @JvmField
        var path: String? = null
        @JvmStatic
        fun saveImage(finalBitmap: Bitmap, name: String, identifier: Int) {
            val root = "$path/$identifier"
            val myDir = File(root)
            myDir.mkdirs()
            val file = File(myDir, name)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.WEBP, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        @JvmStatic
        fun saveTryImage(finalBitmap: Bitmap, name: String, identifier: String) {
            val root = "$path/$identifier"
            val myDir = File("$root/try")
            myDir.mkdirs()
            val fname = name.replace(".png", "").replace(" ", "_") + ".png"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out)
                out.flush()
                out.close()
            } catch (e: Exception) { e.printStackTrace() }
        }

    }
}