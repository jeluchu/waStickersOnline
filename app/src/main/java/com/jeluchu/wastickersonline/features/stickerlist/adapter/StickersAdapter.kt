package com.jeluchu.wastickersonline.features.stickerlist.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.jeluchu.wastickersonline.core.extensions.context.inflate
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackView
import com.jeluchu.wastickersonline.R
import kotlinx.android.synthetic.main.item_sticker.view.*
import kotlin.properties.Delegates

class StickersAdapter : RecyclerView.Adapter<StickersAdapter.ViewHolder>(){
    internal var collection: List<StickerPackView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
}

    internal var clickListener: (StickerPackView) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(R.layout.item_sticker)
        )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(collection[position], clickListener)
    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stickerView: StickerPackView, clickListener: (StickerPackView) -> Unit) {

            itemView.tvPackStickerName.text = stickerView.name
            itemView.ivFirstSticker.load(stickerView.stickers[0].imageFile)
            itemView.ivSecondSticker.load(stickerView.stickers[1].imageFile)
            itemView.ivThirdSticker.load(stickerView.stickers[2].imageFile)

            if (stickerView.stickers.size > 3) {
                itemView.ivFourSticker.load(stickerView.stickers[2].imageFile)
            } else { itemView.ivFourSticker.visibility = View.GONE }

            itemView.setOnClickListener { clickListener(stickerView)}
        }
    }
}