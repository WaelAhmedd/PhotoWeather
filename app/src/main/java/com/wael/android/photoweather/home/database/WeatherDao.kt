package com.wael.android.photoweather.home.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wael.android.photoweather.home.data.Weather

@Dao
interface WeatherDao {

@Insert
fun insertWeather(weather:Weather)

@Query("SELECT * From weather_table")
fun getAllWathers():LiveData<List<Weather>>
}