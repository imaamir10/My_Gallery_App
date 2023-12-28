package com.example.mygalleryapp.feature_gallery.presentation.callback

import com.example.mygalleryapp.feature_gallery.domain.model.MediaData

interface PictureCallback {
    fun onPictureClickListener(mediaData : MediaData)
}