package com.wael.android.photoweather.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.wael.android.photoweather.R
import com.wael.android.photoweather.databinding.FragmentShowWeatherBinding
import com.wael.android.photoweather.home.ViewModels.AddWeatherViewModel
import com.wael.android.photoweather.home.ViewModels.AddWeatherViewModelFactory
import com.wael.android.photoweather.home.ViewModels.ShowHistoryWeatherViewModel
import com.wael.android.photoweather.home.adapters.WeatherAdapter
import com.wael.android.photoweather.home.database.WeatherDatabase

/**
 * A simple [Fragment] subclass.
 */
class ShowWeather : Fragment() {
    lateinit var binding: FragmentShowWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_show_weather, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WeatherDatabase.getInstance(application).weatherDatabaseDao
        val viewModelFactory = AddWeatherViewModelFactory(dataSource, application)
        val showHistoryViewModel =
            ViewModelProvider(this, viewModelFactory).get(ShowHistoryWeatherViewModel::class.java)


        val adapter = WeatherAdapter()
        val manager = activity?.let { GridLayoutManager(it, 2) }
        binding.list.adapter = adapter
        binding.list.layoutManager = manager
        showHistoryViewModel.weatherList.observe(viewLifecycleOwner, Observer { items ->
            if (items.isNotEmpty()) {
                Toast.makeText(context, items[items.size-1].weatherId.toString(), Toast.LENGTH_LONG).show()
                adapter.submitList(items)
            } else {
                Toast.makeText(context, " empty", Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }

}
