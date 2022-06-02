package com.kingsun.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {
    // 定义在内部，防止出现和其他接口的数据模型有同名冲突的情况
    data class AQI(val chn: Float)

    data class AirQuality(val aqi: AQI)

    data class Realtime(
        val temperature: Float,
        val skycon: String,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class Result(val realtime: Realtime)
}