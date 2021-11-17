package com.mehmetboluk.movieapp.data.api

import com.mehmetboluk.movieapp.data.value_object.MovieDetails
import com.mehmetboluk.movieapp.data.value_object.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbInterface {

    //https://api.themoviedb.org/3/movie/550?api_key=87b813952b5320e9cb5ad03daa2fe3f0
    //https://api.themoviedb.org/3/movie/popular?api_key=87b813952b5320e9cb5ad03daa2fe3f0&page=1
    //https://api.themoviedb.org/3/

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page : Int) : Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int) : Single<MovieDetails>
}