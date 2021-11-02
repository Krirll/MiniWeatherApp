package com.learn.learnretrofit

interface MainContract {

    interface MainView {
        fun errorToast(message : String)
        fun<T> startActivity(body : T)
        fun newSettingForButtonAndProgress()
        fun defaultSettingForButtonAndProgress()
    }

    interface GetWeatherInfo {

        interface OnFinishedListener {
            fun <T> onFinished(body: T)
            fun onFailure(t: Throwable)
        }

        fun getWeatherInfo(city : String, onFinishListener : OnFinishedListener)
    }

    interface MainPresenter {
        fun showClick(city: String)
    }
}