package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

// 显示天气信息

/*data class算是Kotlin中一大闪光点，data class就是一个类中只包含一些数据字段，类似于vo,pojo,java bean。
一般而言，我们在Java中定义了这个数据类之后要重写一下toString，equals等方法。要生成get,set方法。
然而在Kotlin中这些都不在需要自己手动去敲了，编译器在背后默默给我们生成了如下的东西：
equals()/hashCode()
toString()方法
componentN()方法
copy()方法*/

/*在申明一个 data class 有一些需要注意的事项。
主构造函数必须要至少有一个参数
主构造函数中的所有参数必须被标记为val或者var
数据类不能有以下修饰符：abstract，inner,open,sealed
data class只能实现接口（Kotlin1.1以前的规则），现在也可以继承其它类*/

// 获取实时天气信息接口所返回的JSON数据格式
data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}