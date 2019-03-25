package ru.iandreyshev.moxyapp.application

import android.app.Application

class MusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        private lateinit var instance: MusicApplication
    }

}
