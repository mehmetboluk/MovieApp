package com.mehmetboluk.movieapp.ui.popularMovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehmetboluk.movieapp.R
import com.mehmetboluk.movieapp.data.api.POSTER_BASE_URL
import com.mehmetboluk.movieapp.data.repository.NetworkState
import com.mehmetboluk.movieapp.data.value_object.Movie
import com.mehmetboluk.movieapp.data.value_object.MovieDetails
import com.mehmetboluk.movieapp.ui.single_movie_details.SingleMovie

class PopularMoviePagedListAdapter(public val context : Context) : PagedListAdapter<Movie,RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.recycyler_row, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie?, context: Context) {
            itemView.findViewById<TextView>(R.id.cvMovieTitle).text = movie?.title
            itemView.findViewById<TextView>(R.id.cvMovieReleaseDate).text = movie?.release_date

            val moviePosterUrl = POSTER_BASE_URL + movie?.poster_path
            Glide.with(itemView.context).load(moviePosterUrl)
                .into(itemView.findViewById(R.id.cvIvMoviePoster))

            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.findViewById<ProgressBar>(R.id.error_progress_bar_item).visibility =
                    View.VISIBLE
            } else {
                itemView.findViewById<ProgressBar>(R.id.error_progress_bar_item).visibility =
                    View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.findViewById<TextView>(R.id.tvErrorMessageItem).visibility = View.VISIBLE
            } else if(networkState != null && networkState == NetworkState.ENDOFLIST){
                itemView.findViewById<TextView>(R.id.tvErrorMessageItem).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.tvErrorMessageItem).visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if (hadExtraRow){                               // hadExtraRow is true and hasExtraRow is false
                notifyItemRemoved(super.getItemCount())     // remove the progress bar at the end
            } else {                                        // has ExtraRow is true and hadExtraRow is false
                notifyItemInserted(super.getItemCount())    // add a progress bar at the end
            }
        }else if(hasExtraRow && previousState != networkState){     //hasExtraRow is true and hadExtraRow is true
            notifyItemChanged(itemCount - 1)
        }
    }
}