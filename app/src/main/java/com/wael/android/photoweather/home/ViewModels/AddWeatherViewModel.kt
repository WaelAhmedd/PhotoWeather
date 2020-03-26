package com.wael.android.photoweather.home.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.wael.android.photoweather.home.data.Weather
import com.wael.android.photoweather.home.database.WeatherDao
import com.wael.android.photoweather.home.database.WeatherDatabase
import com.wael.android.photoweather.home.database.WeatherRepository
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class AddWeatherViewModel(database:WeatherDao,
    application: Application
) : AndroidViewModel(application) {

    private val Repository: WeatherRepository
    val weatherList: LiveData<List<Weather>>
    init {
        val weatherDao=WeatherDatabase.getInstance(application).weatherDatabaseDao
        Repository= WeatherRepository(weatherDao)
        weatherList=Repository.watherList
    }

    fun insert(weather:Weather)=viewModelScope.launch{
        Repository.insertWeather(weather)

    }

}