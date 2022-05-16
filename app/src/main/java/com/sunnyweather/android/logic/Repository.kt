package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.Exception
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }

    // 刷新天气信息
    /*
    * coroutineScope创建了一个协程作用域
    * 在async函数中发起网络请求，然后在分别调用它们的await()方法，就可以保证只有在两个网络请求都成功响应之后，才会进一步执行程序
    * */
    fun refreshWeaher(lng: String, lat: String) = fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeahter(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeahter(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" +
                            "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    // 在函数类型声明一个suspend关键字，以表示传入的Lambda表达式中的代码也是拥有挂起函数上下文的。
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}