package com.example.mygalleryapp.feature_gallery.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.mygalleryapp.databinding.MediaItemBinding
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolder
import java.lang.StringBuilder
import javax.inject.Inject

class MediaItemAdapter @Inject constructor(
    private val glide:RequestManager
) : RecyclerView.Adapter<MediaItemAdapter.MediaFolderViewHolder>(){


    private var mediaFolders = listOf<MediaFolder>()

    fun setData(newList: List<MediaFolder>) {
        val diffCallback = MediaFolderDiffCallback(mediaFolders, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mediaFolders = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaFolderViewHolder {
        val binding = MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaFolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaFolderViewHolder, position: Int) {
        holder.bind(mediaFolders[position])
    }

    override fun getItemCount(): Int {
        return mediaFolders.size
    }

    inner class MediaFolderViewHolder(private var binding: MediaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaFolder: MediaFolder) {
            val builder = StringBuilder()
            builder.append(mediaFolder.folderName)
            builder.append(" ("+mediaFolder.numberOfPics+") ")

            binding.tvFolderName.text = builder.toString()
            glide.load(mediaFolder.firstPic)
                .into(binding.imageView)
        }
    }

    class MediaFolderDiffCallback(private val oldList: List<MediaFolder>, private val newList: List<MediaFolder>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].path == newList[newItemPosition].path
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}