package com.laura.examen.ui.listOfMovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.laura.domain.models.MovieList
import com.laura.examen.R
import com.laura.examen.databinding.FragmentListOfMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import mx.com.satoritech.web.NetworkResult

const val TAG = "MoviesListFragment"
@AndroidEntryPoint
class ListOfMoviesFragment : Fragment(){
    private lateinit var vBind:FragmentListOfMoviesBinding
    private val viewModel:ListOfMoviesViewModel by viewModels()
    private val moviesAdapter = ListOfMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vBind = FragmentListOfMoviesBinding.inflate(inflater, container,false)
        initRv()
        initUpdates()
        initSpinner()
        return vBind.root
    }

    private fun initUpdates(){
        viewModel.getMovies()
        viewModel.updateMoviesByApi()
        viewModel.movies.observe(viewLifecycleOwner){ listWithMovies ->
            moviesAdapter.movieList = listWithMovies
        }
        viewModel.onUpdateMovies.observe(viewLifecycleOwner){ onUpdateMovies ->
            val isLoading = onUpdateMovies is NetworkResult.Loading
            vBind.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
            vBind.spListType.isEnabled = !isLoading
            if(onUpdateMovies is NetworkResult.Error){
                Toast.makeText(context, getString(R.string.err), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onUpdateMovies: "+onUpdateMovies.message)
            }
            if(onUpdateMovies is NetworkResult.Success){
                viewModel.getMovies()
            }
            setupPagination(onUpdateMovies.data?:viewModel.movies.value?.list?: MovieList(), isLoading)
        }
    }

    private fun initRv(){
        vBind.rvMovies.adapter = moviesAdapter
        moviesAdapter.onItemClick = { movie ->
            val action = ListOfMoviesFragmentDirections.moviesListToMovieDetails(movie.movieId)
            findNavController().navigate(action)
        }
    }

    private fun initSpinner(){
        vBind.spListType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, selection: Int, viewId: Long) {
                if(selection+1 != viewModel.type){
                    viewModel.type = selection+1
                    viewModel.page = 1
                    viewModel.getMovies()
                    viewModel.updateMoviesByApi()
                }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}

        }
    }

    private fun setupPagination(movieList: MovieList, isLoading:Boolean){
        val isLastPage = viewModel.page>=movieList.totalPages?:0
        val isFirstPage = viewModel.page == 1
        vBind.bPagPrevius.setOnClickListener {
            viewModel.page--
            viewModel.getMovies()
            viewModel.updateMoviesByApi()
        }
        vBind.bPagNext.setOnClickListener {
            viewModel.page++
            viewModel.getMovies()
            viewModel.updateMoviesByApi()
        }
        vBind.bPagPrevius.visibility = if(!isFirstPage && !isLoading) View.VISIBLE else View.GONE
        vBind.bPagNext.visibility = if(!isLastPage && !isLoading) View.VISIBLE else View.GONE
        movieList.totalPages?.let {
            vBind.tvPagine.text = getString(R.string.pagination_field, viewModel.page.toString(), movieList.totalPages.toString())
        }
    }
}