package com.kingsun.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    // 用于全局获取 context ，这里使用的是 applicationContext ，没有内存泄漏问题
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN ="raZb7wHPVcf6K5Rc"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}