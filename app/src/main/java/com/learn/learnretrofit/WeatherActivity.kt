package com.learn.learnretrofit

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.net.URL
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        val result = intent.getSerializableExtra(RESULT) as Weather
        findViewById<TextView>(R.id.cityResult).text = getString(R.string.city, result.location?.name.toString())
        findViewById<TextView>(R.id.temp).text = getString(R.string.temp, result.current?.temp?.toInt())
        findViewById<TextView>(R.id.lastUpdate).text = getString(R.string.lastUpdate, result.current?.lastUpdated.toString())
        CoroutineScope(Dispatchers.Main).launch {
            val icon = findViewById<ImageView>(R.id.icon)
            val image : Drawable
            withContext(Dispatchers.IO) {
                val url : InputStream = URL("https:${result.current?.condition?.icon}").content as InputStream
                image = Drawable.createFromStream(url, "src")
            }
            icon.setImageDrawable(image)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val RESULT = "RESULT"
    }
}