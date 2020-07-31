package com.jeluchu.wastickersonline

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeluchu.wastickersonline.activity.StickerDetailsActivity
import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.failure
import com.jeluchu.wastickersonline.core.extensions.lifecycle.observe
import com.jeluchu.wastickersonline.features.stickerlist.adapter.StickersAdapter
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.features.stickerlist.viewmodel.StickersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val getStickersView: StickersViewModel by inject()
    private val adapterStickers: StickersAdapter by inject()
    var jsonResult: String = ""
    var outputStream: FileOutputStream? = null

    //var adapter: StickerAdapter? = null
    //var stickerPacks = ArrayList<StickerPack>()
    //var mStickers: MutableList<Sticker>? = null
    //var stickerModels = ArrayList<StickerModel>()
    //var mEmojis: MutableList<String>? = null
    //var mDownloadFiles: MutableList<String>? = null
    //var android_play_store_link: String? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(getStickersView) {
            observe(sticker, ::renderStickersList)
            failure(failure, ::handleFailure)
        }

        loadStickers()
        initListeners()

        //stickerPacks = ArrayList()
        path = "$filesDir/stickers_asset"
        //mStickers = ArrayList()
        //stickerModels = ArrayList()
        //mEmojis = ArrayList()
        //mDownloadFiles = ArrayList()
        //(mEmojis as ArrayList<String>).add("")
        permissions
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        rvStickersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvStickersList.adapter = adapterStickers
        //GetStickers(this, this, resources.getString(R.string.json_link)).execute()
    }

    private fun initListeners() {
        adapterStickers.clickListener = {

            val intent = Intent(this, StickerDetailsActivity::class.java)
            intent.putExtra(EXTRA_STICKERPACK, it)
            startActivity(intent)

        }
    }

    private fun loadStickers() {
        getStickersView.getStickers()
    }

    fun downloadJSON() {

        Thread {


/*
            val `in`: InputStream = URL("https://aruppi.jeluchu.xyz/res/stickers/final.json").openStream()
            val file=File(filesDir, "sticker_packs")
            file.writeText(`in`)
*/

            try {
                val urll = URL("https://aruppi.jeluchu.xyz/res/stickers/final.json")
                val connection = urll.openConnection() as HttpURLConnection
                jsonResult = inputStreamToString(connection.inputStream)
                        .toString()
                Log.i("response", "doInBackground: $jsonResult")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                if (fileExistance("sticker_packs")) {
                    val `is`: InputStream = openFileInput("sticker_packs")
                    val lastWallsFile: String
                    lastWallsFile = inputStreamToString(`is`).toString()
                    Log.e("LastWallsFile:", lastWallsFile)
                    writeWallFile()
                } else {
                    try {
                        writeWallFile()
                    } catch (e: java.lang.Exception) {
                        //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
                    }
                }
            } catch (ex: java.lang.Exception) {
                //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
            }
        }.start()

    }
    private fun writeWallFile() {
        try {
            outputStream = openFileOutput("sticker_packs", Context.MODE_PRIVATE)
            outputStream!!.write(jsonResult.toByteArray())
            outputStream!!.close()
        } catch (ex: java.lang.Exception) {
            //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
        }
    }
    private fun fileExistance(fname: String): Boolean {
        val file: File = getFileStreamPath(fname)
        return file.exists()
    }
    private fun inputStreamToString(`is`: InputStream): StringBuilder? {
        var rLine: String? = ""
        val answer = StringBuilder()
        val rd = BufferedReader(InputStreamReader(`is`))
        try {
            while (rd.readLine().also { rLine = it } != null) {
                answer.append(rLine)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return answer
    }

    private fun renderStickersList(stickersView: List<StickerPackView>?) {
        downloadJSON()
        adapterStickers.collection = stickersView.orEmpty()
    }
    private fun handleFailure(failure: Failure?) {
        failure.toString()
    }

    // We don't have permission so prompt the user
    private val permissions: Unit
        get() {
            val perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (perm != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS,
                        1
                )
            }
        }

    /*override fun onListLoaded(jsonResult: String, jsonSwitch: Boolean) {
        try {
            if (jsonResult != null) {
                try {
                    val jsonResponse = JSONObject(jsonResult)
                    android_play_store_link = jsonResponse.getString("android_play_store_link")
                    val jsonMainNode = jsonResponse.optJSONArray("sticker_packs")
                    Log.d(TAG, "onListLoaded: " + jsonMainNode.length())
                    for (i in 0 until jsonMainNode.length()) {
                        val jsonChildNode = jsonMainNode.getJSONObject(i)
                        Log.d(TAG, "onListLoaded: " + jsonChildNode.getString("name"))
                        stickerPacks.add(StickerPack(
                                jsonChildNode.getString("identifier"),
                                jsonChildNode.getString("name"),
                                jsonChildNode.getString("publisher"),
                                getLastBitFromUrl(jsonChildNode.getString("tray_image_file")).replace(" ", "_"),
                                jsonChildNode.getString("publisher_email"),
                                jsonChildNode.getString("publisher_website"),
                                jsonChildNode.getString("privacy_policy_website"),
                                jsonChildNode.getString("license_agreement_website")
                        ))
                        val stickers = jsonChildNode.getJSONArray("stickers")
                        Log.d(TAG, "onListLoaded: " + stickers.length())
                        for (j in 0 until stickers.length()) {
                            val jsonStickersChildNode = stickers.getJSONObject(j)
                            mStickers!!.add(Sticker(
                                    getLastBitFromUrl(jsonStickersChildNode.getString("image_file")).replace(".png", ".webp"),
                                    mEmojis
                            ))
                            mDownloadFiles!!.add(jsonStickersChildNode.getString("image_file"))
                        }
                        Log.d(TAG, "onListLoaded: " + mStickers!!.size)
                        Hawk.put<List<Sticker>?>(jsonChildNode.getString("identifier"), mStickers)
                        stickerPacks[i].setAndroidPlayStoreLink(android_play_store_link)
                        stickerPacks[i].stickers = Hawk.get(jsonChildNode.getString("identifier"), ArrayList())
                        *//*stickerModels.add(new StickerModel(
                                jsonChildNode.getString("name"),
                                mStickers.get(0).imageFileName,
                                mStickers.get(1).imageFileName,
                                mStickers.get(2).imageFileName,
                                mStickers.get(2).imageFileName,
                                mDownloadFiles
                        ));*//*mStickers!!.clear()
                    }
                    Hawk.put("sticker_packs", stickerPacks)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                adapterStickers.collection = stickerPacks
                adapter = StickerAdapter(this, stickerPacks)
                rvStickersList!!.adapter = adapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d(TAG, "onListLoaded: " + stickerPacks.size)
    }*/

    companion object {
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val EXTRA_STICKERPACK = "stickerpack"

        @JvmField
        var path: String? = null
        @JvmStatic
        fun SaveImage(finalBitmap: Bitmap, name: String, identifier: Int) {
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
        fun SaveTryImage(finalBitmap: Bitmap, name: String, identifier: String) {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun getLastBitFromUrl(url: String): String {
            return url.replaceFirst(".*/([^/?]+).*".toRegex(), "$1")
        }
    }
}