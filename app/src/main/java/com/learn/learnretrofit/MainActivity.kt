package com.learn.learnretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherInfo : MainContract.GetWeatherInfo {
    override fun getWeatherInfo(city : String, onFinishListener : MainContract.GetWeatherInfo.OnFinishedListener) {
        val service = WeatherQueries.create()
        val call = service.getWeatherByCityJSON(city)
        call.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    onFinishListener.onFinished(response.body())
                }
                else
                    onFinishListener.onFailure(Throwable("something went wrong, check your Internet connection"))
            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                onFinishListener.onFailure(t)
            }
        })
    }
}

class Validation private constructor() {
    companion object {
        fun textNotEmpty(string: String) = string != ""
    }
}

class MainPresenter : MainContract.MainPresenter, MainContract.GetWeatherInfo.OnFinishedListener {
    var mainModel : MainContract.GetWeatherInfo? = null
    var mainView : MainContract.MainView? = null
    override fun showClick(city: String) {
        if (Validation.textNotEmpty(city)) {
            mainView?.newSettingForButtonAndProgress()
            mainModel?.getWeatherInfo(city, this)
        }
        else
            mainView?.errorToast("Should not be empty")
    }

    override fun <T> onFinished(body: T) {
        mainView?.startActivity(body)
        mainView?.defaultSettingForButtonAndProgress()
    }

    override fun onFailure(t: Throwable) {
        mainView?.errorToast(t.localizedMessage!!)
    }
}

class MainActivity : AppCompatActivity(), MainContract.MainView {
    private var button : Button? = null
    private var progress : ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = MainPresenter()
        presenter.mainView = this
        presenter.mainModel = WeatherInfo()
        button = findViewById(R.id.show)
        progress = findViewById(R.id.progressBar)
        button?.setOnClickListener {
            presenter.showClick(findViewById<EditText>(R.id.city).text.toString())
        }
    }

    override fun errorToast(message : String) {
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
        defaultSettingForButtonAndProgress()
    }

    override fun<T> startActivity(body: T) {
        startActivity(
            Intent(this@MainActivity, WeatherActivity::class.java).apply {
                putExtra(WeatherActivity.RESULT, body as Weather)
            }
        )
    }

    override fun defaultSettingForButtonAndProgress() {
        button?.isClickable = true
        progress?.visibility = View.GONE
    }

    override fun newSettingForButtonAndProgress() {
        button?.isClickable = false
        progress?.visibility = View.VISIBLE
    }
}