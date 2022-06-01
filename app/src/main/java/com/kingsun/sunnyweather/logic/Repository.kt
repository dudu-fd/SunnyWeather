package com.kingsun.sunnyweather.logic

import androidx.lifecycle.liveData
import com.kingsun.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {

    // 返回一个 LiveData对象 给上层
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) { // IO 线程进行网络请求
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        // 类似于 LiveData 的 setValue 方法
        emit(result)
    }
}