package com.wael.android.photoweather.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wael.android.photoweather.home.data.WeatherResponse
import com.wael.android.photoweather.home.network.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Response
enum class ApiStatus { LOADING, ERROR, DONE }
class HomeViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    private var _WeatherDetails = MutableLiveData<WeatherResponse>()
    val weatherDetails: LiveData<WeatherResponse>
        get() = _WeatherDetails
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    fun getWeather(lat: String, lon: String, appId: String) {
        coroutineScope.launch {

            var getWeatherDefferd =
                WeatherApi.retrofitService.getCurrentWeatherData(lat, lon, appId)
            try{
                _status.value = ApiStatus.LOADING
                var ressult=getWeatherDefferd.await()
                _WeatherDetails.value=ressult
                _status.value = ApiStatus.DONE
            }catch (t:Throwable){
                _status.value = ApiStatus.ERROR
            }

                   /* .enqueue(object : retrofit2.Callback<WeatherResponse> {
                        override fun onResponse(
                            call: Call<WeatherResponse>,
                            response: Response<WeatherResponse>
                        ) {
                            _WeatherDetails.value = response.body()
                            Log.i("Weayher", "hjhj")

                        }

                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                            Log.i("Weather", t.message.toString())
                        }

                    })*/
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {


        var AppId = "b8621222a7986d8c119fb8a38490b22f"
        var lat = "40.4167"
        var lon = "-3.7036"
    }

}