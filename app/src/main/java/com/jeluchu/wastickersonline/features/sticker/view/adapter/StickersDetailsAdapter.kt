package com.jeluchu.wastickersonline.features.sticker.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.inflate
import com.jeluchu.wastickersonline.databinding.ItemStickimageBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerView

class StickersDetailsAdapter :
    ListAdapter<StickerView, StickersDetailsAdapter.ViewHolder>(StickersDiffCallback()) {

    private var clickListener: (StickerView) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(R.layout.item_stickimage)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = getItem(position)
        holder.bind(sticker, clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemStickimageBinding.bind(itemView)

        fun bind(newView: StickerView, clickListener: (StickerView) -> Unit) {
            binding.ivSticker.load(newView.imageFile)
            itemView.setOnClickListener { clickListener(newView) }
        }
    }

    class StickersDiffCallback : DiffUtil.ItemCallback<StickerView>() {
        override fun areItemsTheSame(oldItem: StickerView, newItem: StickerView): Boolean {
            return oldItem.imageFile == newItem.imageFile
        }

        override fun areContentsTheSame(oldItem: StickerView, newItem: StickerView): Boolean = false
    }
}