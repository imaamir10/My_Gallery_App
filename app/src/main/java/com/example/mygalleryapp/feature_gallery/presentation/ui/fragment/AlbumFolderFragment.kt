package com.example.mygalleryapp.feature_gallery.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.mygalleryapp.R
import com.example.mygalleryapp.databinding.FragmentAlbumFolderBinding
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.domain.model.MediaData
import com.example.mygalleryapp.feature_gallery.presentation.adapter.AlbumItemAdapter
import com.example.mygalleryapp.feature_gallery.presentation.callback.PictureCallback
import com.example.mygalleryapp.feature_gallery.presentation.vm.GalleryViewModel
import com.example.mygalleryapp.utils.ItemMarginDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class AlbumFolderFragment: Fragment(R.layout.fragment_album_folder), PictureCallback {
    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var albumAdapter : AlbumItemAdapter

    private var fragmentBinding : FragmentAlbumFolderBinding?= null

    private val galleryViewModel : GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: AlbumFolderFragmentArgs by navArgs()
        val receivedMediaFolder: MediaFolderData = args.mediaData
        val binding = FragmentAlbumFolderBinding.bind(view)
        fragmentBinding = binding
        setupAlbumGrid()
        fetchData(receivedMediaFolder)
    }
    private fun setupAlbumGrid() {
        fragmentBinding?.rvAlbumGrid?.adapter = albumAdapter
        fragmentBinding?.rvAlbumGrid?.addItemDecoration(ItemMarginDecoration(requireContext()))
        albumAdapter.setItemClickListener(this)
    }

    private fun fetchData(receivedMediaFolder: MediaFolderData){
        if(!receivedMediaFolder.allImages.isNullOrEmpty()){
            receivedMediaFolder.allImages?.toList()?.let { albumAdapter.setData(it) }
        }else {
            lifecycleScope.launch {
                galleryViewModel.mediaData.collect { folders ->
                    albumAdapter.setData(folders)
                }
            }
            receivedMediaFolder.path?.let {
                galleryViewModel.fetchPictureData(it, requireContext().contentResolver)
            }
        }

    }


    override fun onPictureClickListener(mediaData: MediaData) {

    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}