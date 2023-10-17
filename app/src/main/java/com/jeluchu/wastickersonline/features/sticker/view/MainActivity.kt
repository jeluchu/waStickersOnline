package com.jeluchu.wastickersonline.features.sticker.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsTiramisuAndUp
import com.jeluchu.wastickersonline.core.extensions.permissionStorage
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.wastickersonline.core.ui.navigation.Navigation
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //private val binding by viewBinding(ActivityMainBinding::inflate)

    //private val adapterStickers: StickersAdapter by inject()

    private val preferences by lazy { SharedPrefsHelpers() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)

        /*with(getStickersView) {
            observe(sticker, ::renderStickersList)
           // failure(failure, ::handleFailure)
        }*/

        setContent {
            MaterialTheme {
                Navigation()
            }
        }

        initRequiresConfig()
        //initListeners()
    }


    private fun initRequiresConfig() {
        path = "$filesDir/stickers_asset"
        permissionStorage
    }

    /*private fun initListeners() {
        adapterStickers.clickListener = {
            openActivity(StickerDetailsActivity::class.java) {
                putParcelable(EXTRA_STICKERPACK, it)
            }
            openActivityRight()
        }
        onBackPressedDispatcher.addCallback(
            this@MainActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = exitActivityBottom()
            }
        )
    }*/

    companion object {
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val EXTRA_STICKERPACK = "stickerpack"

        @JvmField
        var path: String? = null

    }
}

val StickerPackType = object : NavType<StickerPack>(isNullableAllowed = false) {
    override fun put(bundle: Bundle, key: String, value: StickerPack) =
        bundle.putParcelable(key, value)

    override fun get(bundle: Bundle, key: String): StickerPack? =
        if (buildIsTiramisuAndUp) bundle.getParcelable(key, StickerPack::class.java)
        else bundle.getParcelable(key)

    override fun parseValue(value: String): StickerPack = Json.decodeFromString(Uri.decode(value))
}