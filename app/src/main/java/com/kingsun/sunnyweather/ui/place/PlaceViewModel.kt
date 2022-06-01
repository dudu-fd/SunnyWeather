package com.kingsun.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kingsun.sunnyweather.logic.Repository
import com.kingsun.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
    // 内部用于搜索请求的参数监听对象
    private val searchLiveData = MutableLiveData<String>()

    // 用于缓存列表数据
    val placeList = ArrayList<Place>()

    // 外部用到的数据列表监听对象
    val placeLiveData = Transformations.switchMap(searchLiveData) {
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}