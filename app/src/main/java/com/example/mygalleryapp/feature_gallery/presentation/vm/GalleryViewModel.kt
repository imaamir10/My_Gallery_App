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
import com.example.mygalleryapp.feature_gallery.domain.model.MediaData
import com.example.mygalleryapp.feature_gallery.domain.usecase.GetAllMediaFilesFromFolderUseCase
import kotlinx.coroutines.launch


@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val useCaseGetMediaFolders: GetMediaFolderUseCase,
    private val useCaseGetPictures:GetAllMediaFilesFromFolderUseCase
): ViewModel() {
    // StateFlow to handle media folders
    private val _mediaFolders = MutableStateFlow<List<MediaFolderData>>(emptyList())
    val mediaFolders: StateFlow<List<MediaFolderData>> get() = _mediaFolders


    // StateFlow to handle media data pictures and videos
    private val _mediaData = MutableStateFlow<List<MediaData>>(emptyList())
    val mediaData: StateFlow<List<MediaData>> get() = _mediaData


    // Fetch media folders from the specified content resolver
    fun fetchMediaFolders(contentResolver: ContentResolver) {

        viewModelScope.launch {
            useCaseGetMediaFolders.invoke(contentResolver)
                .collect { folders ->
                    _mediaFolders.value = folders
                }
        }
    }


    // Fetch picture data based on the provided path and content resolver
    fun fetchPictureData(path:String, contentResolver: ContentResolver) {

        viewModelScope.launch {
            useCaseGetPictures.invoke(path,contentResolver)
                .collect { folders ->
                    _mediaData.value = folders
                }
        }
    }
}