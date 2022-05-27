package mx.com.satoritech.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laura.database.convertes.DBTypeConverters
import com.laura.database.dao.MovieDao
import com.laura.database.dao.MovieListDao
import com.laura.domain.models.Movie
import com.laura.domain.models.MovieList
import com.laura.domain.models.MoviesListRef

@Database(entities = [Movie::class, MovieList::class, MoviesListRef::class], version = 1)
@TypeConverters(DBTypeConverters::class)
abstract class MoviesDB: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieListDao(): MovieListDao

    companion object{
        @JvmStatic
        fun newInstance(context:Context): MoviesDB {
            return Room.databaseBuilder(context,MoviesDB::class.java,"AppDB")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}