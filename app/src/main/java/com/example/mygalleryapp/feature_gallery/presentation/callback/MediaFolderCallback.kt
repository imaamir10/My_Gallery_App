package com.example.mygalleryapp.feature_gallery.presentation.callback

import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData

interface MediaFolderCallback {
    fun onMediaFolderClickListener(mediaFolder: MediaFolderData)
}