package com.jeluchu.wastickersonline.core.di

import androidx.multidex.BuildConfig
import com.jeluchu.jchucomponents.utils.network.RetrofitClient
import com.jeluchu.jchucomponents.utils.network.interceptors.Interceptor
import com.jeluchu.jchucomponents.utils.network.interceptors.InterceptorHeaders
import com.jeluchu.wastickersonline.WaStickersOnline
import com.jeluchu.wastickersonline.core.utils.Enviroments
import com.jeluchu.wastickersonline.features.sticker.repository.StickersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitClient.buildRetrofit(
        Enviroments.getApiEndpointStickers(),
        GsonConverterFactory.create(),
        WaStickersOnline.getContext(),
        Interceptor(
            InterceptorHeaders(
                userAgent = InterceptorHeaders.UserAgent(
                    appName = "WaStickersOnline",
                    versionName = BuildConfig.VERSION_NAME,
                    versionCode = BuildConfig.VERSION_CODE
                ),
                client = "WaStickersOnline"
            )
        ),
        BuildConfig.DEBUG
    )

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): RetrofitClient = RetrofitClient

    @Provides
    fun provideStickersRepository(
        dataSource: StickersRepository.StickersRepositoryImpl
    ): StickersRepository = dataSource
}