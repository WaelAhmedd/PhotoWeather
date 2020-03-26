package com.wael.android.photoweather.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wael.android.photoweather.databinding.WeatherListItemBinding
import com.wael.android.photoweather.home.data.Weather


class WeatherAdapter : ListAdapter<Weather, WeatherAdapter.ViewHolder>(WeatherCallBacks()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: WeatherListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Weather) {
            binding.weather = item
//          bin
            binding.executePendingBindings()

            //binding.recipeHomeImage.setImageDrawable(R.drawable.ic_clouddd)
            //binding.recipeHomeImage.setImageBitmap(bitmap)


        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherListItemBinding.inflate(layoutInflater, parent, false)
                return WeatherAdapter.ViewHolder(binding)
            }
        }
    }

    class WeatherCallBacks : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.weatherId == newItem.weatherId
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }

    }


}