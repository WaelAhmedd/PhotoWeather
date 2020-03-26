package com.wael.android.photoweather.home.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wael.android.photoweather.home.data.Weather
import com.wael.android.photoweather.home.database.WeatherDao
import com.wael.android.photoweather.home.database.WeatherDatabase
import com.wael.android.photoweather.home.database.WeatherRepository
import kotlinx.coroutines.launch

class ShowHistoryWeatherViewModel(database: WeatherDao,
                          application: Application
) : AndroidViewModel(application) {

    private val Repository: WeatherRepository
    val weatherList: LiveData<List<Weather>>
    init {
        val weatherDao= WeatherDatabase.getInstance(application).weatherDatabaseDao
        Repository= WeatherRepository(weatherDao)
        weatherList=Repository.watherList
    }



}