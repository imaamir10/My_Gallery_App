package com.example.mygalleryapp.feature_gallery.presentation.vm

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.domain.usecase.GetMediaFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.mygalleryapp.feature_gallery.domain.model.PictureData
import com.example.mygalleryapp.feature_gallery.domain.usecase.GetAllPicturesFromFolderUseCase
import kotlinx.coroutines.launch


@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val useCaseGetMediaFolders: GetMediaFolderUseCase,
    private val useCaseGetPictures:GetAllPicturesFromFolderUseCase
): ViewModel() {

    private val _mediaFolders = MutableStateFlow<List<MediaFolderData>>(emptyList())
    val mediaFolders: StateFlow<List<MediaFolderData>> get() = _mediaFolders

    private val _pictureData = MutableStateFlow<List<PictureData>>(emptyList())
    val pictureData: StateFlow<List<PictureData>> get() = _pictureData

    fun fetchMediaFolders(contentResolver: ContentResolver) {

        viewModelScope.launch {
            useCaseGetMediaFolders.invoke(contentResolver)
                .collect { folders ->
                    _mediaFolders.value = folders
                }
        }
    }

    fun fetchPitureData(path:String,contentResolver: ContentResolver) {

        viewModelScope.launch {
            useCaseGetPictures.invoke(path,contentResolver)
                .collect { folders ->
                    _pictureData.value = folders
                }
        }
    }
}