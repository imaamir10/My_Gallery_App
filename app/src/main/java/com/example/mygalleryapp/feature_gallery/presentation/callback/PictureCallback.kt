package com.example.mygalleryapp.feature_gallery.presentation.callback

import com.example.mygalleryapp.feature_gallery.domain.model.PictureData

interface PictureCallback {
    fun onPictureClickListener(pictureData : PictureData)
}