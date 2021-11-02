package com.learn.learnretrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

class Location : Serializable {
    @SerializedName ("name")
    @Expose
    var name : String? = null
}

class Current : Serializable {
    @SerializedName ("temp_c")
    @Expose
    var temp : Double? = null

    @SerializedName ("last_updated")
    @Expose
    var lastUpdated : String? = null

    @SerializedName ("condition")
    @Expose
    var condition : Condition? = null
}

class Condition : Serializable {
    @SerializedName ("icon")
    @Expose
    var icon : String? = null
}

class Weather : Serializable {
    @SerializedName ("location")
    @Expose
    var location : Location? = null

    @SerializedName ("current")
    @Expose
    var current : Current? = null
}


interface WeatherQueries {
    @GET("/v1/current.json?key=cdb4b6587cca4fb5b5c54908212709&aqi=no")
    fun getWeatherByCityJSON(@Query("q") city : String) : Call<Weather>
    companion object {
        fun create() : WeatherQueries {
            return Retrofit.Builder()
                            .baseUrl("https://api.weatherapi.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(WeatherQueries::class.java)
        }
    }
}