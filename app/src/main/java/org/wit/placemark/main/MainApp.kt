package org.wit.placemark.main


import android.app.Application
import timber.log.Timber
import timber.log.Timber.i
import org.wit.placemark.models.PlacemarkModel

class MainApp : Application() {
    val placemarks = ArrayList<PlacemarkModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Placemark started")
    }
}