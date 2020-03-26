package com.wael.android.photoweather.home.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wael.android.photoweather.home.data.Weather

@Database(entities = [Weather::class],version = 1220,exportSchema = false)
abstract class WeatherDatabase :RoomDatabase(){
    abstract val weatherDatabaseDao:WeatherDao
    companion object{

        @Volatile
        private var INSTANCE:WeatherDatabase?=null

        fun getInstance(context: Context): WeatherDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
    }

}}