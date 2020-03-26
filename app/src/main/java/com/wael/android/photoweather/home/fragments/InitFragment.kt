package com.wael.android.photoweather.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*

import com.wael.android.photoweather.R
import com.wael.android.photoweather.databinding.FragmentLocationBinding
import com.wael.android.photoweather.home.ApiStatus
import com.wael.android.photoweather.home.HomeViewModel
import com.wael.android.photoweather.home.data.Weather
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */


class InitFragment : Fragment() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    val PERMISSION_ID = 42
    lateinit var viewModel: HomeViewModel
    lateinit var weather: Weather
    lateinit var address: List<Address>
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var binding: FragmentLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        mFusedLocationClient =
            context?.let { LocationServices.getFusedLocationProviderClient(it) }!!

        getLastLocation()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer { status ->
            if (status == ApiStatus.LOADING) {
                binding.progressBar1.show()
            }
            if (status == ApiStatus.ERROR) {
                binding.noInternet.visibility = View.VISIBLE
                binding.cloud.visibility = View.INVISIBLE
                binding.refreshTv.visibility=View.VISIBLE
                binding.refreshIcon.visibility=View.VISIBLE
                binding.refreshIcon.setOnClickListener { getLastLocation() }
            }
        })
        return binding.root
    }

    fun move() {
        viewModel.getWeather(
            latitude.toString(),
            longitude.toString(),
            "b8621222a7986d8c119fb8a38490b22f"
        )
        viewModel.weatherDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            address = getCity(latitude, longitude)
            val temp = viewModel.weatherDetails.value?.main?.temp_min.toString()
            val city = address[0].getAddressLine(0)
            findNavController().navigate(
                InitFragmentDirections.actionInitFragmentToHomeFragment(
                    temp, city


                )
            )
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                activity?.let {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(it) { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude = location.latitude
                            longitude = location.longitude


                            address = getCity(latitude, longitude)
                            Toast.makeText(context, "Welcome ! ", Toast.LENGTH_LONG)
                                .show()
                            weather = Weather()
                            //latitude.toString(),longitude.toString(),address[0].getAddressLine(0)

                            move()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }


    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient =
            context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            Toast.makeText(context, latitude.toString(), Toast.LENGTH_LONG).show()
            address = getCity(latitude, longitude)
            Toast.makeText(context, address[0].getAddressLine(0), Toast.LENGTH_LONG).show()
            weather = Weather()
            //latitude.toString(),longitude.toString(),address[0].getAddressLine(0)
            //findNavController().navigate(InitFragmentDirections.actionInitFragmentToHomeFragment(weather))

            move()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_ID
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    fun getCity(lat: Double, long: Double): List<Address> {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
        //  val cityName: String = addresses[0].getAddressLine(0)
        return addresses
    }


}

