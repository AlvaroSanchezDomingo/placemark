package org.wit.placemark.views.editlocation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import org.wit.placemark.databinding.ActivityMapsBinding
import org.wit.placemark.databinding.ContentEditLocationBinding
import org.wit.placemark.databinding.ContentPlacemarkMapsBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location


class EditLocationView : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener
    {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var contentBinding: ContentEditLocationBinding
    private lateinit var presenter: EditLocationPresenter
    lateinit var app: MainApp
    private lateinit var map: GoogleMap
    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp
        location = intent.extras?.getParcelable<Location>("location")!!
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = EditLocationPresenter(this)
        contentBinding = ContentEditLocationBinding.bind(binding.root)
        contentBinding.lat.text = "%.6f".format(location.lat)
        contentBinding.lng.text = "%.6f".format(location.lng)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            map = it
            presenter.initMap(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        presenter.initMap(googleMap)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,marker.position.longitude, map.cameraPosition.zoom)
        contentBinding.lat.text = "%.6f".format(marker.position.latitude)
        contentBinding.lng.text = "%.6f".format(marker.position.longitude)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()

    }
}