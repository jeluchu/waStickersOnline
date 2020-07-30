package com.jeluchu.wastickersonline.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.jeluchu.wastickersonline.R
import java.io.IOException
import java.util.*

class StickerDetailsAdapter(var strings: ArrayList<String>, var context: Context) : RecyclerView.Adapter<StickerDetailsAdapter.ViewHolder>() {
    var id: String? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {

            val image = if (strings[i].split("/").size == 1) {
                "https://aruppi.jeluchu.xyz/res/stickers/${strings[i]}"

            } else {
                "https://aruppi.jeluchu.xyz/res/stickers/${strings[i].split("/")[8]}"
            }

            viewHolder.imageView.load(image)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = strings.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.sticker_image)

    }

}