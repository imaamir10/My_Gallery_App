package com.example.mygalleryapp.ui.feature_gallery.di

import com.example.mygalleryapp.ui.feature_gallery.data.local.repository.GalleryRepositoryImpl
import com.example.mygalleryapp.ui.feature_gallery.domain.repository.GalleryRepository
import com.example.mygalleryapp.ui.feature_gallery.domain.usecase.GetMediaFolderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Provides
    @Singleton
    fun provideGetMediaFolderUseCase(repository: GalleryRepository): GetMediaFolderUseCase = GetMediaFolderUseCase(repository)

    @Provides
    @Singleton
    fun provideGalleryRepositoryImp() : GalleryRepository = GalleryRepositoryImpl()
}