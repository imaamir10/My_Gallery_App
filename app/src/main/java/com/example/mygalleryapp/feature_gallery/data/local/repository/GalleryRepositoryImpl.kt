package com.example.mygalleryapp.feature_gallery.data.local.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.domain.model.MediaData
import com.example.mygalleryapp.feature_gallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class GalleryRepositoryImpl : GalleryRepository {
    override suspend fun getMediaFolders(contentResolver: ContentResolver): Flow<List<MediaFolderData>> =
        flow {

            val mediaFolders = ArrayList<MediaFolderData>()
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
                        val mediaFolder = MediaFolderData()
                        val mediaDataPath =
                            it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                        val mediaName =
                            it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                        val mediaType =
                            it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

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
                                mediaFolder.addMedia()
                            }

                            mediaFolders.add(mediaFolder)


                        } else {
                            for (i in mediaFolders.indices) {
                                if (mediaFolders[i].path == mediaFolderPath) {
                                    mediaFolders[i].firstPic = mediaDataPath

                                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ||
                                        mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                                    ) {
                                        mediaFolders[i].addMedia()
                                    }


                                }
                            }
                        }
                    } while (it.moveToNext())
                }
            }
            var allImagesFolder = MediaFolderData()
            val pictures = getAllPicturesFromDevice(contentResolver)
            pictures.collect() { list ->
                allImagesFolder.apply {
                    folderName = "All Images"
                    numberOfMediaFiles = list.size
                    allImages = ArrayList(list)
                    firstPic = list.firstOrNull()?.picturePath

                }
            }

            var allVideoFolder = MediaFolderData()
            val videos = getAllVideosFromDevice(contentResolver)
            videos.collect(){list->
                allVideoFolder.apply {
                    folderName = "All Videos"
                    numberOfMediaFiles = list.size
                    allImages = ArrayList(list)
                    firstPic = list.firstOrNull()?.picturePath
                }
            }


            mediaFolders.add(0, allImagesFolder)
            mediaFolders.add(1, allVideoFolder)
            emit(mediaFolders)
        }

    private suspend fun getAllVideosFromDevice(contentResolver: ContentResolver): Flow<List<MediaData>> = flow {
            val videos = mutableListOf<MediaData>()

            val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE
            )

            val cursor = contentResolver.query(
                videoUri,
                projection,
                null,
                null,
                null
            )

            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val video = MediaData().apply {
                        pictureName =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                        picturePath =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        pictureSize =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                        isFileVideo = true
                    }
                    videos.add(video)
                }
                emit(videos.toList())
            }
        }

    private suspend fun getAllPicturesFromDevice(contentResolver: ContentResolver): Flow<List<MediaData>> =
        flow {
            val images = mutableListOf<MediaData>()

            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
            )

            val selection = "${MediaStore.Images.Media.DATA} NOT LIKE ? AND " +
                    "${MediaStore.Images.Media.DATA} NOT LIKE ? AND " +
                    "${MediaStore.Images.Media.DATA} NOT LIKE ?"
            val selectionArgs = arrayOf(
                "%/cache/%",
                "%/thumbnails/%",
                "%/nomedia/%"
            )
            val cursor = contentResolver.query(
                imageUri,
                projection,
                selection,
                selectionArgs,
                null
            )

            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val pic = MediaData().apply {
                        pictureName =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                        picturePath =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        pictureSize =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    }
                    images.add(pic)

                    emit(images.toList())
                }
            }
        }

    override suspend fun getAllMediaFromSpecificFolder(
        path: String,
        contentResolver: ContentResolver
    ): Flow<List<MediaData>> = flow {

        val mediaData = mutableListOf<MediaData>()

        val allMediaUri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )

        val selection =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) AND " +
                    "${MediaStore.Files.FileColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            "%$path%"
        )

        contentResolver.query(
            allMediaUri,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val isVideo = path.endsWith(".mp4") ||
                        path.endsWith(".avi") ||
                        path.endsWith(".3gp") ||
                        path.endsWith(".mkv") ||
                        path.endsWith(".webm") ||
                        path.endsWith(".flv") ||
                        path.endsWith(".wmv")
                val pic = MediaData().apply {
                    pictureName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    picturePath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    pictureSize =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    isFileVideo = isVideo
                }
                mediaData.add(pic)
            }
        }
        emit(mediaData)
    }

}