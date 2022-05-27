package com.laura.examen.ui.directions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laura.domain.models.LocationPoint
import com.laura.examen.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.com.satoritech.web.NetworkResult
import javax.inject.Inject

@HiltViewModel
class DirectionsViewModel @Inject constructor(
    val locationRepository: LocationRepository
): ViewModel(){
    private val _locations = MutableLiveData<NetworkResult<List<LocationPoint>>>()

    val locations: LiveData<NetworkResult<List<LocationPoint>>> = _locations

    fun getLocations() = viewModelScope.launch(Dispatchers.IO){
        locationRepository.getUserLocations().collect{
            _locations.postValue(it)
        }
    }
}