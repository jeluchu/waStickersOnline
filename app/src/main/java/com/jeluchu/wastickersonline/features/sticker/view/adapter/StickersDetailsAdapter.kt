package com.jeluchu.wastickersonline.features.sticker.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.inflate
import com.jeluchu.wastickersonline.databinding.ItemStickimageBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerView
import kotlin.properties.Delegates

class StickersDetailsAdapter : RecyclerView.Adapter<StickersDetailsAdapter.ViewHolder>(){
    internal var collection: List<StickerView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
}

    internal var clickListener: (StickerView) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                    parent.inflate(R.layout.item_stickimage)
            )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(collection[position], clickListener)
    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemStickimageBinding.bind(itemView)

        fun bind(newView: StickerView, clickListener: (StickerView) -> Unit) {
            binding.ivSticker.load(newView.imageFile)
            itemView.setOnClickListener { clickListener(newView)}
        }
    }
}