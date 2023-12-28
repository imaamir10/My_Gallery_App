package com.example.mygalleryapp.feature_gallery.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaData(
    var pictureName: String? = null,
    var picturePath: String? = null,
    var pictureSize: String? = null,
    var imageUri: Int = 0,
    var firstPic: String? = null,
    var isFileVideo: Boolean = false
):Parcelable
