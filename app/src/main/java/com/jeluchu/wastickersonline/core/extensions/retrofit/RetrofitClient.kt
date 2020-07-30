package com.jeluchu.wastickersonline.core.extensions.retrofit

import com.jeluchu.wastickersonline.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var CONNECT_TIMEOUT = 60L
    private var READ_TIMEOUT = 60L
    private var WRITE_TIMEOUT = 60L

    fun buildRetrofit(baseUrl: String, converterFactory: Converter.Factory): Retrofit? {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createClientBuilder().build())
                .addConverterFactory(converterFactory)
                .build()
    }

    private fun createClientBuilder(): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(makeLoggingInterceptor())
        }
        return clientBuilder
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val debugInterceptor = HttpLoggingInterceptor()
        debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return debugInterceptor
    }

}

