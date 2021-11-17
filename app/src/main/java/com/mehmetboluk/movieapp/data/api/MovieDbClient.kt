package com.mehmetboluk.movieapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//https://api.themoviedb.org/3/movie/popular?api_key=87b813952b5320e9cb5ad03daa2fe3f0&page=1
//https://api.themoviedb.org/3/movie/550?api_key=87b813952b5320e9cb5ad03daa2fe3f0
//https://image.tmdb.org/t/p/w500/a26cQPRhJPX6GbWfQbvZdrrp9j9.jpg
const val API_KEY = "87b813952b5320e9cb5ad03daa2fe3f0"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500/"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

object MovieDbClient {

    fun getClient() : MovieDbInterface {
        val requestInterceptor = Interceptor {
            val url = it.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor it.proceed(request)
        }

        val okHTTPClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHTTPClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieDbInterface::class.java)
    }
}