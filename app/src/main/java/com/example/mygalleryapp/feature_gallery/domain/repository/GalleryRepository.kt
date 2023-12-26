package com.example.mygalleryapp.feature_gallery.domain.repository

import android.content.ContentResolver
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolder
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolder>>
}