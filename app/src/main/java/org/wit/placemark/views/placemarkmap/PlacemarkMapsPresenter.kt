package org.wit.placemark.views.placemarkmap

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.placemark.main.MainApp

class PlacemarkMapsPresenter(private val view: PlacemarkMapsView) {
    var app: MainApp = view.application as MainApp

    init {
        app = view.application as MainApp
    }


    fun configureMap(map: GoogleMap) {
        map.setOnMarkerClickListener(view)
        map.uiSettings.isZoomControlsEnabled = true

        app.placemarks.findAll().forEach{
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun findPlacemarkById(marker: Marker) {
        val tag = marker.tag as Long
        val placemark = app.placemarks.findById(tag)
        view.showPlacemarkOnCard(placemark!!)

    }




}