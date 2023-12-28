package com.example.mygalleryapp.feature_gallery.domain.usecase

import android.content.ContentResolver
import com.example.mygalleryapp.feature_gallery.domain.repository.GalleryRepository

class GetAllPicturesFromFolderUseCase (private val repository: GalleryRepository) {
    suspend operator fun invoke(path: String,contentResolver: ContentResolver) =
        repository.getAllMediaFromSpecificFolder(path,contentResolver)
}
