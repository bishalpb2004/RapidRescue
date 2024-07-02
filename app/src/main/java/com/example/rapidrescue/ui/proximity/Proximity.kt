package com.example.rapidrescue.ui.proximity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rapidrescue.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import android.graphics.Color

class Proximity : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var progressBar: ProgressBar
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val DEFAULT_ZOOM_LEVEL = 6.0
    private val USER_AGENT = "YOUR_APP_NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_proximity, container, false)
        mapView = view.findViewById(R.id.map_view)
        mapView.setMultiTouchControls(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        mapView.overlays.add(myLocationOverlay)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermission()
        view.findViewById<Button>(R.id.btnPolice).setOnClickListener { showShortestPath("police") }
        view.findViewById<Button>(R.id.btnHospital).setOnClickListener { showShortestPath("hospital") }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            progressBar.visibility = View.VISIBLE
            myLocationOverlay.enableMyLocation()
            myLocationOverlay.runOnFirstFix {
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                    val myLocation = myLocationOverlay.myLocation
                    if (myLocation != null) {
                        mapView.controller.setZoom(15.0)
                        mapView.controller.setCenter(myLocation)
                    } else {
                        mapView.controller.setZoom(DEFAULT_ZOOM_LEVEL)
                    }
                }
            }
        }
    }

    private fun showShortestPath(placeType: String) {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val location = myLocationOverlay.myLocation
                if (location == null) {
                    Log.d("Proximity", "Current location is not available.")
                    return
                }
                val start = GeoPoint(location.latitude, location.longitude)
                val end = findNearestPlace(start, placeType)
                if (end == null) {
                    Log.d("Proximity", "No $placeType found nearby.")
                    return
                }

                val roadManager = OSRMRoadManager(requireContext(), USER_AGENT)
                val waypoints = arrayListOf(start, end)
                Thread {
                    val road = roadManager.getRoad(waypoints)
                    if (road.mStatus != Road.STATUS_OK) {
                        Log.e("Proximity", "Error calculating the road: ${road.mStatus}")
                    }
                    activity?.runOnUiThread {
                        mapView.overlays.removeAll { it !is MyLocationNewOverlay }
                        val roadOverlay = RoadManager.buildRoadOverlay(road)
                        roadOverlay.color = Color.RED // Set the route color to red
                        mapView.overlays.add(roadOverlay)
                        mapView.overlays.add(createMarker(start, "Your location", R.drawable.location_start))
                        mapView.overlays.add(createMarker(end, placeType.capitalize(), R.drawable.location_end))
                        mapView.invalidate()
                        mapView.controller.setCenter(start)
                        mapView.controller.setZoom(15.0) // Zoom in a bit more after clicking
                    }
                }.start()
            }
        } catch (e: Exception) {
            Log.e("Proximity", "Error showing shortest path: ${e.message}", e)
        }
    }

    private fun findNearestPlace(currentLocation: GeoPoint, placeType: String): GeoPoint? {
        // Implement your logic to find the nearest place of the specified type
        // This is a placeholder method, replace with your actual implementation
        // Example implementation:
        if (placeType == "police") {
            // Find nearest police station coordinates
            return GeoPoint(currentLocation.latitude + 0.01, currentLocation.longitude + 0.01) // Example coordinates
        } else if (placeType == "hospital") {
            // Find nearest hospital coordinates
            return GeoPoint(currentLocation.latitude + 0.02, currentLocation.longitude + 0.02) // Example coordinates
        }
        return null
    }
    private fun createMarker(geoPoint: GeoPoint, title: String, iconResource: Int): Marker {
        return Marker(mapView).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            this.title = title
            icon = ContextCompat.getDrawable(requireContext(), iconResource)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }
}
