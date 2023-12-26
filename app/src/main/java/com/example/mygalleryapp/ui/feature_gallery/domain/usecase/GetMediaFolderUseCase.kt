package com.example.mygalleryapp.ui.feature_gallery.domain.usecase

import android.content.ContentResolver
import com.example.mygalleryapp.ui.feature_gallery.domain.repository.GalleryRepository

class GetMediaFolderUseCase(private val repository: GalleryRepository) {
    suspend operator fun invoke(contentResolver: ContentResolver) = repository.getMediaFolders(contentResolver)
}