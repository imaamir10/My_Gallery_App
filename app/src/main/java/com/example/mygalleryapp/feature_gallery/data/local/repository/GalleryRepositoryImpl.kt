package com.example.mygalleryapp.feature_gallery.data.local.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolder
import com.example.mygalleryapp.feature_gallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class GalleryRepositoryImpl: GalleryRepository {
    override suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolder>>  = flow {
        val mediaFolders = ArrayList<MediaFolder>()
        val mediaPaths = ArrayList<String>()

        val allMediaUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )

        val selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " +
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        val cursor = contentResolver.query(
            allMediaUri,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val mediaFolder = MediaFolder()
                    val mediaDataPath = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                    val mediaName = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                    val mediaType = it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

                    val file = File(mediaDataPath)
                    val mediaFolderPath = file.parent

                    if (!mediaPaths.contains(mediaFolderPath)) {
                        mediaPaths.add(mediaFolderPath)

                        mediaFolder.path = mediaFolderPath
                        mediaFolder.folderName = file.parentFile?.name ?: ""
                        mediaFolder.firstPic = mediaDataPath

                        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ||
                            mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                        ) {
                            mediaFolder.addPics()
                        }

                        mediaFolders.add(mediaFolder)
                    } else {
                        for (i in mediaFolders.indices) {
                            if (mediaFolders[i].path == mediaFolderPath) {
                                mediaFolders[i].firstPic = mediaDataPath

                                if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ||
                                    mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                                ) {
                                    mediaFolders[i].addPics()
                                }
                            }
                        }
                    }
                } while (it.moveToNext())
            }
        }

//        mediaFolders.forEach {
//            println("Media Folders: ${it.folderName}, Path = ${it.path}, Pics: ${it.numberOfPics}")
//        }

        emit(mediaFolders)
    }
}