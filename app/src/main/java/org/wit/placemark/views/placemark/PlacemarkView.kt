package org.wit.placemark.views.placemark

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.views.placemark.PlacemarkPresenter
import timber.log.Timber.i

class PlacemarkView : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var presenter: PlacemarkPresenter
    var placemark = PlacemarkModel()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PlacemarkPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }


        binding.mapView.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.placemarkTitle.text.toString().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        R.string.enter_placemark_title,
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    presenter.doAddOrSave(
                        binding.placemarkTitle.text.toString(),
                        binding.description.text.toString()
                    )
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun showPlacemark(placemark: PlacemarkModel) {
        binding.placemarkTitle.setText(placemark.title)
        binding.description.setText(placemark.description)

        Picasso.get()
            .load(placemark.image)
            .into(binding.placemarkImage)
        if (placemark.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.button_changeImage)
        }
        binding.lat.text = "%.6f".format(placemark.lat)
        binding.lng.text = "%.6f".format(placemark.lng)
    }
    fun locationUpdate(placemark: PlacemarkModel) {
        binding.lat.text = "%.6f".format(placemark.lat)
        binding.lng.text = "%.6f".format(placemark.lng)
    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.placemarkImage)
        binding.chooseImage.setText(R.string.button_changeImage)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}