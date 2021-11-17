package com.mehmetboluk.movieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.mehmetboluk.movieapp.data.api.MovieDbInterface
import com.mehmetboluk.movieapp.data.repository.MovieDetailsNetworkDataSource
import com.mehmetboluk.movieapp.data.repository.NetworkState
import com.mehmetboluk.movieapp.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService : MovieDbInterface) {

    lateinit var movieDetailsNetworkDataSource : MovieDetailsNetworkDataSource

    fun fetchingSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId : Int) : LiveData<MovieDetails>  {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState() : LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}