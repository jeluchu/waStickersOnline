package com.jeluchu.wastickersonline.features.sticker.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.observe
import com.jeluchu.wastickersonline.core.extensions.others.exitActivityBottom
import com.jeluchu.wastickersonline.core.extensions.others.openActivity
import com.jeluchu.wastickersonline.core.extensions.others.openActivityRight
import com.jeluchu.wastickersonline.core.extensions.others.statusBarColor
import com.jeluchu.wastickersonline.core.utils.hawk.Hawk
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersAdapter
import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

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


        path = "$filesDir/stickers_asset"

        permissions
        setContentView(R.layout.activity_main)
        statusBarColor()
        initListeners()
        rvStickersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvStickersList.adapter = adapterStickers

    }

    private fun initListeners() {
        adapterStickers.clickListener = {
            openActivity(StickerDetailsActivity::class.java) {
                putParcelable(EXTRA_STICKERPACK, it)
            }
            openActivityRight()
        }
    }

    private fun loadStickers() = getStickersView.getStickers()

    private fun renderStickersList(stickersView: List<StickerPackView>?) {
        val stickerPack : ArrayList<StickerPackView> = stickersView as ArrayList<StickerPackView>
        Hawk.put("sticker_packs", stickerPack)
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

    override fun onBackPressed() {
        super.onBackPressed()
        exitActivityBottom()
    }

    companion object {
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val EXTRA_STICKERPACK = "stickerpack"

        @JvmField
        var path: String? = null

    }

}