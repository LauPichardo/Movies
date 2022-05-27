package com.laura.examen.ui.listOfMovies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.laura.domain.models.ListWithMovies
import com.laura.domain.models.Movie
import com.laura.examen.R
import com.laura.examen.databinding.ItemMovieBinding
import mx.com.satoritech.web.APIConstants

class ListOfMoviesAdapter : RecyclerView.Adapter<ListOfMoviesAdapter.moviesViewHoder>() {
    var onItemClick:(Movie)->Unit = {}
    var movieList: ListWithMovies? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListOfMoviesAdapter.moviesViewHoder {
        val inflater = LayoutInflater.from(parent.context)
        val vBind = ItemMovieBinding.inflate(inflater, parent, false)
        return moviesViewHoder(parent.context, vBind)
    }

    override fun onBindViewHolder(holder: ListOfMoviesAdapter.moviesViewHoder, position: Int) {
        val item = movieList?.movies?.get(position)?: Movie()
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return movieList?.movies?.size?:0
    }

    inner class moviesViewHoder(
        private val context: Context,
        private val vBind: ItemMovieBinding
    ):RecyclerView.ViewHolder(vBind.root){

        fun onBind(movie: Movie){
            vBind.tvMovieName.setText(movie.title)
            vBind.tvMoviePopularity.text = context.getString(R.string.popularity_field, (movie.popularity?:0.0).toString())
            vBind.tvMovieRate.text = context.getString(R.string.rate_field, (movie.voteAverage?:0.0).toString())
            vBind.ivMovieImage.load(APIConstants.imageServerPath+movie.backdropPath){
                this.crossfade(true)
            }
            vBind.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

}