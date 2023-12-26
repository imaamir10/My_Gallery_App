package com.example.mygalleryapp.ui.feature_gallery.presentation.vm

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.example.mygalleryapp.ui.feature_gallery.domain.model.MediaFolder
import com.example.mygalleryapp.ui.feature_gallery.domain.usecase.GetMediaFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val useCase: GetMediaFolderUseCase
): ViewModel() {

    private val _mediaFolders = MutableStateFlow<List<MediaFolder>>(emptyList())
    val mediaFolders: StateFlow<List<MediaFolder>> get() = _mediaFolders.asStateFlow()

    fun fetchMediaFolders(contentResolver: ContentResolver) {

        viewModelScope.launch {
            useCase.invoke(contentResolver)
                .collect { folders ->
                    _mediaFolders.value = folders
                }
        }
    }
}