package com.example.mygalleryapp.feature_gallery.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mygalleryapp.databinding.PictureItemBinding
import com.example.mygalleryapp.feature_gallery.domain.model.MediaData
import com.example.mygalleryapp.feature_gallery.presentation.callback.PictureCallback
import javax.inject.Inject

class AlbumItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<AlbumItemAdapter.PictureViewHolder>(){


    private var mediaData = listOf<MediaData>()
    private var pictureCallback : PictureCallback?= null

    /**
     * Set item click listener for the picture.
     */
    fun setItemClickListener(pictureCallback: PictureCallback){
        this.pictureCallback = pictureCallback
    }
    private fun getItem(position: Int): MediaData {
        return mediaData[position]
    }

    /**
     * Update adapter data with a new list of media items.
     */
    fun setData(newList: List<MediaData>) {
        Log.e("data",newList.toString())
        val diffCallback = PictureDiffCallback(mediaData, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mediaData = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(mediaData[position])
    }

    override fun getItemCount(): Int {
        return mediaData.size
    }

    inner class PictureViewHolder(private var binding: PictureItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = getItem(position)
                    pictureCallback?.onPictureClickListener(clickedItem)
                }
            }
        }
        fun bind(mediaFolder: MediaData) {
            glide.load(mediaFolder.picturePath)
                .apply(RequestOptions().centerCrop())
                .into(binding.ivPicture)

            binding.ivPlay.visibility = if (mediaFolder.isFileVideo) View.VISIBLE else View.GONE
        }
    }

    /**
     * DiffUtil Callback for calculating differences between old and new lists of media items.
     */

    class PictureDiffCallback(private val oldList: List<MediaData>, private val newList: List<MediaData>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].picturePath == newList[newItemPosition].picturePath
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}