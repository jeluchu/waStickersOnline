package com.jeluchu.wastickersonline.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.MainActivity
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.activity.StickerDetailsActivity
import com.jeluchu.wastickersonline.adapter.StickerDetailsAdapter
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerView
import java.io.File
import java.util.*

class StickerDetailsActivity : AppCompatActivity() {
    var adapter: StickerDetailsAdapter? = null
    var toolbar: Toolbar? = null
    var stickers: List<StickerView>? = null
    var strings: ArrayList<String>? = null
    var addtowhatsapp: Button? = null
    var stickerPackView: StickerPackView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_details)
        //stickerPack = getIntent().getParcelableExtra(MainActivity.EXTRA_STICKERPACK);
        toolbar = findViewById(R.id.toolbar)
        addtowhatsapp = findViewById(R.id.add_to_whatsapp)
        /*        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stickerPack.name);
        getSupportActionBar().setSubtitle(stickerPack.publisher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/if (intent.extras != null) {
            stickerPackView = intent.getSerializableExtra("stickerpack") as StickerPackView?
        }
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
        adapter = StickerDetailsAdapter(strings!!, this)
        val gridLayoutManager = GridLayoutManager(this, 4)
        rvStickersPack.setLayoutManager(gridLayoutManager)
        rvStickersPack.setAdapter(adapter)
        addtowhatsapp.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent()
            intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            intent.putExtra(MainActivity.EXTRA_STICKER_PACK_ID, stickerPackView!!.identifier)
            intent.putExtra(MainActivity.EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
            intent.putExtra(MainActivity.EXTRA_STICKER_PACK_NAME, stickerPackView!!.name)
            try {
                startActivityForResult(intent, ADD_PACK)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this@StickerDetailsActivity, "error", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        private const val ADD_PACK = 200
        var path: String? = null
        private fun getLastBitFromUrl(url: String): String {
            return url.replaceFirst(".*/([^/?]+).*".toRegex(), "$1")
        }
    }
}