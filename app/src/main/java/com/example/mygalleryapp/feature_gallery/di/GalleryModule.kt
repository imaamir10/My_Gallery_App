package com.example.mygalleryapp.feature_gallery.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mygalleryapp.R
import com.example.mygalleryapp.feature_gallery.data.local.repository.GalleryRepositoryImpl
import com.example.mygalleryapp.feature_gallery.domain.repository.GalleryRepository
import com.example.mygalleryapp.feature_gallery.domain.usecase.GetMediaFolderUseCase
import com.example.mygalleryapp.feature_gallery.presentation.adapter.MediaItemAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {
    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )

    @Provides
    fun provideUserAdapter(glide: RequestManager): MediaItemAdapter {
        return MediaItemAdapter(glide)
    }

    @Provides
    @Singleton
    fun provideGetMediaFolderUseCase(repository: GalleryRepository): GetMediaFolderUseCase = GetMediaFolderUseCase(repository)

    @Provides
    @Singleton
    fun provideGalleryRepositoryImp() : GalleryRepository = GalleryRepositoryImpl()
}