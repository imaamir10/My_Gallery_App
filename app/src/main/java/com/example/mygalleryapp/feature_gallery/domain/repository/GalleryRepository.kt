package com.example.mygalleryapp.feature_gallery.domain.repository

import android.content.ContentResolver
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.domain.model.MediaData
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolderData>>
    suspend fun getAllMediaFromSpecificFolder(path :String, contentResolver: ContentResolver) : Flow<List<MediaData>>
}