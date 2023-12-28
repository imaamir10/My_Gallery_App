package com.example.mygalleryapp.feature_gallery.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaFolderData (
    var path: String? = null,
    var folderName: String? = null,
    var numberOfMediaFiles: Int = 0,
    var numberOfPictures: Int = 0,
    var firstPic: String? = null,
    var allImages : ArrayList<MediaData> ?=null

    ):Parcelable{
    fun addMedia() {
        numberOfMediaFiles++
    }
}