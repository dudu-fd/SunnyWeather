package com.kingsun.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName


data class Location(val lng:String,val lat:String)

// 把响应返回的 formatted_address 字段映射成 address
data class Place(val name:String,val location: Location,@SerializedName("formatted_address") val address:String)

data class PlaceResponse(val status:String,val places: List<Place>)
