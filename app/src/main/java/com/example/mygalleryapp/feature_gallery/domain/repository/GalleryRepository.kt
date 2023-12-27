package com.example.mygalleryapp.feature_gallery.domain.repository

import android.content.ContentResolver
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.domain.model.PictureData
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolderData>>
    suspend fun getAllMediafromFolder(path :String, contentResolver: ContentResolver) : Flow<List<PictureData>>
}