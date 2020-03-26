package com.wael.android.photoweather.home.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.wael.android.photoweather.home.data.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WeatherRepository(private val weatherDao: WeatherDao) {
    val watherList: LiveData<List<Weather>> = weatherDao.getAllWathers()
    suspend fun insertWeather(weather: Weather) {
        withContext(Dispatchers.IO) {
            weatherDao.insertWeather(weather)
        }

    }
}