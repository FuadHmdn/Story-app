package com.fuad.story_app.ui.view

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fuad.story_app.R
import com.fuad.story_app.databinding.ActivityMapsBinding
import com.fuad.story_app.ui.viewmodel.StoryViewModel
import com.fuad.story_app.ui.viewmodel.UserViewModel
import com.fuad.story_app.ui.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val storyViewModel: StoryViewModel by viewModels { factory }
    private val userViewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        userViewModel.getTokenUser.observe(this) {
            storyViewModel.getAllStory(it)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        storyViewModel.listStoryItem.observe(this) { items ->
            if (items != null) {
                for (item in items) {
                    item?.let {
                        val location = LatLng(it.lat!!, it.lon!!)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(location)
                                .title(it.name)
                                .snippet(it.description)
                        )
                    }
                }
                val indonesia = LatLng(-6.2088, 106.8456)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 5f))
            }
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this ,R.raw.map_style))

            if (!success) {
                Log.d(TAG, "Gagal mengatur Style Map")
            }
        } catch (e: Resources.NotFoundException) {
            Log.d(TAG, "Style tidak ditemukan: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}