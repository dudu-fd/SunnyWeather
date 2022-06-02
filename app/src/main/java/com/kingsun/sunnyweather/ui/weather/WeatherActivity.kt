package com.kingsun.sunnyweather.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.kingsun.sunnyweather.R
import com.kingsun.sunnyweather.databinding.*
import com.kingsun.sunnyweather.logic.model.Weather
import com.kingsun.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    lateinit var binding: ActivityWeatherBinding

     val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)

        // 全屏并透明状态栏
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        setContentView(binding.root)

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        viewModel.weatherLiveData.observe(this) {
            val weather = it.getOrNull()
            if (weather == null) {
                Toast.makeText(this, "获取天气信息失败", Toast.LENGTH_SHORT).show()
            } else {
                showWeatherInfo(weather)
            }
            binding.swipeRefresh.isRefreshing = false
        }
        refreshWeather()
        binding.swipeRefresh.setColorSchemeResources(R.color.purple_700)
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.nowLayout.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.addDrawerListener(object :DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerClosed(drawerView: View) {
                // 关闭的时候，隐藏输入法
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather (){
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    @SuppressLint("SetTextI18n")
    private fun showWeatherInfo(weather: Weather) {
        binding.nowLayout.placeName.text = viewModel.placeName

        val realtime = weather.realtime
        val daily = weather.daily
        // 填充 now 布局的数据
        binding.nowLayout.apply {
            currentTemp.text = "${realtime.temperature.toInt()} ℃"
            currentSky.text = getSky(realtime.skycon).info
            currentAQI.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
            rootLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        }
        // 填充 forecast 布局的数据
        binding.forecastLayout.apply {
            forecastLayout.removeAllViews()
            for (i in 0 until daily.skycon.size) {
                val skycon = daily.skycon[i]
                val sky = getSky(skycon.value)
                ForecastItemBinding.inflate(layoutInflater,forecastLayout,false)
                    .apply {
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateInfo.text = simpleDateFormat.format(skycon.date)
                        skyIcon.setImageResource(sky.icon)
                        skyInfo.text = sky.info
                        temperatureInfo.text =
                            "${daily.temperature[i].min.toInt()} ~ ${daily.temperature[i].max.toInt()} ℃"
                        forecastLayout.addView(root)
                    }
            }
        }
        // 填充 life_index 布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndexLayout.apply {
            coldRiskText.text = lifeIndex.coldRisk[0].desc
            dressingText.text = lifeIndex.dressing[0].desc
            ultravioletText.text = lifeIndex.ultraviolet[0].desc
            carWashingText.text = lifeIndex.carWashing[0].desc
        }
        binding.weatherLayout.visibility = View.VISIBLE
    }
}