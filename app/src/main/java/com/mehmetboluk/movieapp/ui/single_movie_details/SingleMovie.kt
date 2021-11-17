package com.mehmetboluk.movieapp.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mehmetboluk.movieapp.data.api.MovieDbClient
import com.mehmetboluk.movieapp.data.api.MovieDbInterface
import com.mehmetboluk.movieapp.data.api.POSTER_BASE_URL
import com.mehmetboluk.movieapp.data.repository.NetworkState
import com.mehmetboluk.movieapp.data.value_object.MovieDetails
import com.mehmetboluk.movieapp.databinding.ActivityMovieDetailsBinding
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository : MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    val movieId : Int = intent.getIntExtra("id", 1)

    val apiService : MovieDbInterface = MovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, androidx.lifecycle.Observer {
         bindUI(it)
        })

        viewModel.netWorkState.observe(this, androidx.lifecycle.Observer {
            binding.progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    fun bindUI(it : MovieDetails){
        binding.tvMovieTitle.text = it.title
        binding.tvMovieTagLine.text = it.tagline
        binding.tvMovieRealiseDate.text = it.release_date
        binding.tvMovieRating.text = it.vote_average.toString()
        binding.tvMovieRuntime.text = it.runtime.toString() + " minutes"
        binding.tvMovieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.tvMovieBudget.text = formatCurrency.format(it.budget)
        binding.tvMovieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterUrl = POSTER_BASE_URL + it.poster_path
        Glide.with(this).load(moviePosterUrl).into(binding.ivMoviePoster)
    }

    fun getViewModel(movieId: Int) : SingleMovieViewModel {
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}