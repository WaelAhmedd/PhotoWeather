package com.wael.android.photoweather.home.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wael.android.photoweather.home.database.WeatherDao

class AddWeatherViewModelFactory(private val database:WeatherDao,
                                 private val application: Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddWeatherViewModel::class.java)) {
            return AddWeatherViewModel(database, application) as T
        }
        if (modelClass.isAssignableFrom(ShowHistoryWeatherViewModel::class.java)) {
            return ShowHistoryWeatherViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}