package com.example.mygalleryapp.ui.feature_gallery.domain.repository

import android.content.ContentResolver
import com.example.mygalleryapp.ui.feature_gallery.domain.model.MediaFolder
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolder>>
}