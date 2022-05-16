package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

// 单例类
object PlaceDao {

    // 将Place对象存储到sharedPreferences文件中
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }

    // 先将JSON字符串从sharedPreferences文件中读取出来，然后再通过GSON将JSON字符串解析成Place对象并返回
    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    // 判断是否有数据已经被存储
    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",
        Context.MODE_PRIVATE)
}