package com.aravindcraj.px.network

import com.aravindcraj.px.data.models.SearchPhotos
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PxService {
    @GET("services/rest/")
    suspend fun searchPhotos(
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): Response<SearchPhotos>

    companion object {
        private const val BASE_URL = "https://api.flickr.com/"

        fun create(): PxService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(PxService::class.java)
        }
    }
}