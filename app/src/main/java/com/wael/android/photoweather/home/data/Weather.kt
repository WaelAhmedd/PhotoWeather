package com.wael.android.photoweather.home.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Weather_table")
@Parcelize
data class Weather(
    @PrimaryKey(autoGenerate = true)
    var weatherId: Long = 0L,
    var place: String? = null,
    var weatherCondition: String? = null,
    var city: String? = null,
    var temp: String? = null,
    var imagePath: String? = null
) : Parcelable