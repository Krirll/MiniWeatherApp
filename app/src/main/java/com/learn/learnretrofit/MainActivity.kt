package com.learn.learnretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.show)
        button.setOnClickListener {
            val text = findViewById<TextView>(R.id.city)
            if (text.text.isNotEmpty()) {
                button.isClickable = false
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.weatherapi.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                Log.d("RETROFIT", "build")
                val service = retrofit.create(WeatherQueries::class.java)
                val progress = findViewById<ProgressBar>(R.id.progressBar)
                progress.visibility = View.VISIBLE
                val call = service.getWeatherByCity(text.text.toString())
                call.enqueue(object : Callback<Weather> {
                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        if (response.isSuccessful) {
                            startActivity(
                                Intent(this@MainActivity, WeatherActivity::class.java).apply {
                                    putExtra(WeatherActivity.RESULT, response.body() as Weather)
                                }
                            )
                            progress.visibility = View.GONE
                            button.isClickable = true
                        }
                        else
                            makeToast(getString(R.string.incorrectCityName), button, progress)
                    }
                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        makeToast(getString(R.string.loadError), button, progress)
                    }
                })
            }
            else
                makeToast(getString(R.string.emptyError), button)
        }
    }
    private fun makeToast(message : String, button: Button, progress : ProgressBar? = null) {
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
        button.isClickable = true
        progress?.visibility = View.GONE
    }
}