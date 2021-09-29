package com.learn.learnretrofit

import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.net.URL
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ProgressBar
import kotlinx.coroutines.*
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
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            icon.setImageDrawable(image)
            //вращение изображения с использованием нелинейной интерполяции
            ObjectAnimator.ofFloat(icon, "rotation", 360f).apply {
                duration = 1500
                setEvaluator(FloatEvaluator())
                interpolator = AnticipateOvershootInterpolator()
                start()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        const val RESULT = "RESULT"
    }
}