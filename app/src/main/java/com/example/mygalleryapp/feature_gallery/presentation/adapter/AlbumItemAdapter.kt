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
import com.example.mygalleryapp.feature_gallery.domain.model.PictureData
import com.example.mygalleryapp.feature_gallery.presentation.callback.PictureCallback
import javax.inject.Inject

class AlbumItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<AlbumItemAdapter.PictureViewHolder>(){


    private var pictureData = listOf<PictureData>()
    private var pictureCallback : PictureCallback?= null
    fun setItemClickListener(pictureCallback: PictureCallback){
        this.pictureCallback = pictureCallback
    }
    private fun getItem(position: Int): PictureData {
        return pictureData[position]
    }
    fun setData(newList: List<PictureData>) {
        Log.e("data",newList.toString())
        val diffCallback = PictureDiffCallback(pictureData, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        pictureData = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictureData[position])
    }

    override fun getItemCount(): Int {
        return pictureData.size
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
        fun bind(mediaFolder: PictureData) {
            glide.load(mediaFolder.picturePath)
                .apply(RequestOptions().centerCrop())
                .into(binding.ivPicture)

            if(mediaFolder.isFileVideo)
                binding.ivPlay.visibility = View.VISIBLE
            else
                binding.ivPlay.visibility = View.GONE
        }
    }

    class PictureDiffCallback(private val oldList: List<PictureData>, private val newList: List<PictureData>) : DiffUtil.Callback() {
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