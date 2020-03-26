package com.wael.android.photoweather.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wael.android.photoweather.R
import com.wael.android.photoweather.databinding.FragmentHomeBinding
import com.wael.android.photoweather.home.HomeViewModel
import com.wael.android.photoweather.home.data.Main
import com.wael.android.photoweather.home.data.Weather


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    val PERMISSION_ID = 42
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var main: Main
    lateinit var wather:Weather
    lateinit var args:Weather
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(arguments!=null){
            val args = HomeFragmentArgs.fromBundle(arguments!!)
            wather= Weather()
            wather.city=args.city
            wather.temp=args.temp
        }

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        main = Main()


        binding.weather=wather
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.facebook.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddWeather(wather)) }
            binding.saved.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_showWeathers) }
        return binding.root
    }


}
