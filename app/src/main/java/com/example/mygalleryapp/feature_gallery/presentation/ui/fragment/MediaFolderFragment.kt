package com.example.mygalleryapp.feature_gallery.presentation.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.mygalleryapp.R
import com.example.mygalleryapp.databinding.FragmentMediaFolderBinding
import com.example.mygalleryapp.feature_gallery.domain.model.MediaFolderData
import com.example.mygalleryapp.feature_gallery.presentation.adapter.MediaItemAdapter
import com.example.mygalleryapp.feature_gallery.presentation.callback.MediaFolderCallback
import com.example.mygalleryapp.feature_gallery.presentation.vm.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MediaFolderFragment : Fragment(R.layout.fragment_media_folder), MediaFolderCallback {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var mediaItemAdapter: MediaItemAdapter

    private var fragmentBinding : FragmentMediaFolderBinding ?= null
    private val galleryViewModel : GalleryViewModel by viewModels()
    private val STORAGE_PERMISSION_CODE = 23
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMediaFolderBinding.bind(view)
        fragmentBinding = binding
        setupMediaGrid()
        checkAndFetchData()
    }
    private fun setupMediaGrid() {
        fragmentBinding?.rvMediaGrid?.adapter = mediaItemAdapter
        mediaItemAdapter.setItemClickListener(this)
    }

    private fun checkAndFetchData() {
        if (!checkStoragePermissions()) {
            requestForStoragePermissions()
        } else {
            fetchData()
        }
    }

    private fun fetchData(){
        lifecycleScope.launch {
            galleryViewModel.mediaFolders.collect { folders ->
                mediaItemAdapter.setData(folders)
            }
        }
        galleryViewModel.fetchMediaFolders(requireContext().contentResolver)
    }

    override fun onMediaFolderClickListener(mediaFolder: MediaFolderData) {
        val action = MediaFolderFragmentDirections.actionMediaFolderFragmentToAlbumFolderFragment(mediaFolder)
        findNavController().navigate(action)
    }

    private fun requestForStoragePermissions() {
        //Android is 11 (R) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            //Below android 11
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
            )
        }
    }

    private val storageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //Android is 11 (R) or above
                    if (Environment.isExternalStorageManager()) {
                        //Manage External Storage Permissions Granted
                        fetchData()
                        Log.d("error", "onActivityResult: Manage External Storage Permissions Granted")
                    } else {
                        Toast.makeText(requireContext(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                    }
                }
            })

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val read = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (read ) {
                    fetchData()
                    Toast.makeText(requireContext(), "Storage Permissions Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun checkStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val read = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }


}