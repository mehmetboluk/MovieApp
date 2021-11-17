package com.mehmetboluk.movieapp.ui.popularMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mehmetboluk.movieapp.data.api.MovieDbInterface
import com.mehmetboluk.movieapp.data.api.POSTER_BASE_URL
import com.mehmetboluk.movieapp.data.api.POST_PER_PAGE
import com.mehmetboluk.movieapp.data.repository.MovieDataSource
import com.mehmetboluk.movieapp.data.repository.MovieDataSourceFactory
import com.mehmetboluk.movieapp.data.repository.NetworkState
import com.mehmetboluk.movieapp.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService : MovieDbInterface) {

    lateinit var moviePagedList : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory : MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory,config).build()

        return moviePagedList
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}