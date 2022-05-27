package com.laura.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laura.domain.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: List<Movie>)

    @Query("SELECT * FROM Movie WHERE movieId = :movieId LIMIT 1")
    suspend fun getMovie(movieId:Long):Movie?


}