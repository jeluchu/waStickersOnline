package com.jeluchu.wastickersonline.features.sticker.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeluchu.aruppi.core.extensions.viewbinding.viewBinding
import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.observe
import com.jeluchu.wastickersonline.core.extensions.others.exitActivityBottom
import com.jeluchu.wastickersonline.core.extensions.others.openActivity
import com.jeluchu.wastickersonline.core.extensions.others.openActivityRight
import com.jeluchu.wastickersonline.core.extensions.others.statusBarColor
import com.jeluchu.wastickersonline.core.extensions.permissionStorage
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.wastickersonline.databinding.ActivityMainBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersAdapter
import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val getStickersView: StickersViewModel by inject()
    private val adapterStickers: StickersAdapter by inject()

    private val preferences by lazy { SharedPrefsHelpers() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(getStickersView) {
            observe(sticker, ::renderStickersList)
            failure(failure, ::handleFailure)
        }

        initRequiresConfig()
        statusBarColor()
        initListeners()
    }

    private fun initRequiresConfig() {
        path = "$filesDir/stickers_asset"
        permissionStorage
    }

    private fun initListeners() {
        adapterStickers.clickListener = {
            openActivity(StickerDetailsActivity::class.java) {
                putParcelable(EXTRA_STICKERPACK, it)
            }
            openActivityRight()
        }
    }

    private fun renderStickersList(stickersView: List<StickerPackView>?) {
        preferences.saveObjectsList("sticker_packs", stickersView)
        adapterStickers.collection = stickersView.orEmpty()
        binding.rvStickersList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = adapterStickers
            scheduleLayoutAnimation()
        }
    }
    private fun handleFailure(failure: Failure?) = Log.d("waStickersOnline", failure.toString())

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