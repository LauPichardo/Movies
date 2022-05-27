package com.laura.examen.repository


import com.laura.domain.models.LocationPoint
import kotlinx.coroutines.flow.Flow
import mx.com.satoritech.web.NetworkResult

interface LocationRepository {
    fun getUserLocation(): Flow<LocationPoint>
    suspend fun getUserLocations(): Flow<NetworkResult<List<LocationPoint>>>
    suspend fun saveUserLocation(locationPoint: LocationPoint)
}