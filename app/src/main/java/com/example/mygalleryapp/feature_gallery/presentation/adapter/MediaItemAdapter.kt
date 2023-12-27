package com.example.mygalleryapp.feature_gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mygalleryapp.databinding.MediaItemBinding
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.presentation.callback.MediaFolderCallback
import java.lang.StringBuilder
import javax.inject.Inject

class MediaItemAdapter @Inject constructor(
    private val glide:RequestManager
) : RecyclerView.Adapter<MediaItemAdapter.MediaFolderViewHolder>(){


    private var mediaFolders = listOf<MediaFolderData>()
    private var mediaFolderCallback : MediaFolderCallback ?= null
    fun setItemClickListener(mediaFolderCallback : MediaFolderCallback?){
        this.mediaFolderCallback = mediaFolderCallback
    }
    private fun getItem(position: Int): MediaFolderData {
        return mediaFolders[position]
    }
    fun setData(newList: List<MediaFolderData>) {
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

        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = getItem(position)
                    mediaFolderCallback?.onMediaFolderClickListener(clickedItem)
                }
            }
        }
        fun bind(mediaFolder: MediaFolderData) {
            val builder = StringBuilder()
            builder.append(mediaFolder.folderName)
            builder.append(" ("+mediaFolder.numberOfMediaFiles+") ")

            binding.tvFolderName.text = builder.toString()
            glide.load(mediaFolder.firstPic)
                .into(binding.imageView)
        }
    }

    class MediaFolderDiffCallback(private val oldList: List<MediaFolderData>, private val newList: List<MediaFolderData>) : DiffUtil.Callback() {
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