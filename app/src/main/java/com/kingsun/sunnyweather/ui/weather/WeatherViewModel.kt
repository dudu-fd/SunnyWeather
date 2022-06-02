package com.kingsun.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kingsun.sunnyweather.logic.Repository
import com.kingsun.sunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    private val locationLiveData = MutableLiveData<Location>()

    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshWeather(it.lng, it.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}