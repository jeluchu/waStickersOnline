package com.jeluchu.wastickersonline.features.sticker.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.inflate
import com.jeluchu.wastickersonline.databinding.ItemStickerBinding
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackView

class StickersAdapter :
    ListAdapter<StickerPackView, StickersAdapter.ViewHolder>(StickersDiffCallback()) {

    internal var clickListener: (StickerPackView) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(R.layout.item_sticker)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = getItem(position)
        holder.bind(sticker, clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemStickerBinding.bind(itemView)

        fun bind(stickerView: StickerPackView, clickListener: (StickerPackView) -> Unit) =
            with(binding) {

                binding.tvPackStickerName.text = stickerView.name
                binding.ivFirstSticker.load(stickerView.stickers[0].imageFile)
                binding.ivSecondSticker.load(stickerView.stickers[1].imageFile)
                binding.ivThirdSticker.load(stickerView.stickers[2].imageFile)

                if (stickerView.stickers.size > 3) {
                    binding.ivFourSticker.load(stickerView.stickers[3].imageFile)
                } else {
                    binding.ivFourSticker.visibility = View.GONE
                }

                if (stickerView.stickers.size > 4) {
                    binding.ivFourSticker.load(stickerView.stickers[4].imageFile)
                } else {
                    binding.ivFourSticker.visibility = View.GONE
                }

                if (stickerView.stickers.size > 5) {
                    binding.ivFiveSticker.load(stickerView.stickers[5].imageFile)
                } else {
                    binding.ivFiveSticker.visibility = View.GONE
                }

                if (stickerView.stickers.size > 6) {
                    binding.ivSixSticker.load(stickerView.stickers[6].imageFile)
                } else {
                    binding.ivSixSticker.visibility = View.GONE
                }

                itemView.setOnClickListener { clickListener(stickerView) }
            }
    }

    class StickersDiffCallback : DiffUtil.ItemCallback<StickerPackView>() {
        override fun areItemsTheSame(oldItem: StickerPackView, newItem: StickerPackView): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(
            oldItem: StickerPackView,
            newItem: StickerPackView
        ): Boolean = false
    }
}