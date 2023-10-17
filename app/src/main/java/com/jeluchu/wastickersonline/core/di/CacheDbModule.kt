package com.jeluchu.wastickersonline.core.di

import android.content.Context
import androidx.room.Room
import com.jeluchu.wastickersonline.core.database.AppDatabase
import com.jeluchu.wastickersonline.core.utils.Database
import com.jeluchu.wastickersonline.features.sticker.repository.local.StickersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheDbModule {

    @Provides
    @Singleton
    fun provideCacheDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, AppDatabase::class.java, Database.cache)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideStickerDao(appDatabase: AppDatabase): StickersDao =
        appDatabase.stickerDao()
}