package com.laura.examen.ui.listOfMovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laura.domain.models.ListWithMovies
import com.laura.domain.models.MovieList
import com.laura.examen.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.satoritech.web.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ListOfMoviesViewModel  @Inject constructor(
    private val moviesRepository: MoviesRepository
): ViewModel() {
    private val _movies = MutableLiveData<ListWithMovies?>()
    private val _onUpdateMovies = MutableLiveData<NetworkResult<MovieList>>()

    val movies: LiveData<ListWithMovies?> = _movies
    val onUpdateMovies: LiveData<NetworkResult<MovieList>> = _onUpdateMovies
    var page:Int = 1
    var type:Int = 1

    fun getMovies() = viewModelScope.launch(Dispatchers.IO){
        moviesRepository.getList(type, page).collect{
            _movies.postValue(it)
        }
    }

    fun updateMoviesByApi() = viewModelScope.launch(Dispatchers.IO){
        moviesRepository.updateListbyApi(type, page).collect{
            _onUpdateMovies.postValue(it)
        }
    }
}