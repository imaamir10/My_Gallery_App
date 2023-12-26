package com.example.mygalleryapp.feature_gallery.domain.model

data class MediaFolder (
    var path: String? = null,
    var folderName: String? = null,
    var numberOfPics: Int = 0,
    var firstPic: String? = null
){
    fun addPics() {
        numberOfPics++
    }
}