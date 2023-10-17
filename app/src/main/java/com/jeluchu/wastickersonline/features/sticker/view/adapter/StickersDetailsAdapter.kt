package com.jeluchu.wastickersonline.features.sticker.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.extensions.others.inflate
import com.jeluchu.wastickersonline.databinding.ItemStickimageBinding
import com.jeluchu.wastickersonline.features.sticker.models.Sticker
import com.jeluchu.wastickersonline.features.sticker.view.StickersPreviewDialog

class StickersDetailsAdapter :
    ListAdapter<Sticker, StickersDetailsAdapter.ViewHolder>(StickersDiffCallback()) {

    lateinit var supportFragmentManager: FragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(R.layout.item_stickimage)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = getItem(position)
        holder.bind(sticker, supportFragmentManager)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemStickimageBinding.bind(itemView)

        fun bind(newView: Sticker, supportFragmentManager: FragmentManager) {
            binding.ivSticker.load(newView.imageFile)
            itemView.setOnClickListener {
                StickersPreviewDialog(
                    newView.imageFile
                ).show(supportFragmentManager, "StickersPreviewDialog")
            }
        }
    }

    class StickersDiffCallback : DiffUtil.ItemCallback<Sticker>() {
        override fun areItemsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
            return oldItem.imageFile == newItem.imageFile
        }

        override fun areContentsTheSame(oldItem: Sticker, newItem: Sticker): Boolean = false
    }
}