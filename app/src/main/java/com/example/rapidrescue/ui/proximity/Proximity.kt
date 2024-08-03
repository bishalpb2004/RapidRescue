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
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.rapidrescue.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException
import kotlin.concurrent.thread

class Proximity : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingContainer: LinearLayout
    private lateinit var blurView: View
    private lateinit var loadingText: TextView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val DEFAULT_ZOOM_LEVEL = 15
    private val USER_AGENT = "YOUR_APP_NAME"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_proximity, container, false)
        mapView = view.findViewById(R.id.map_view)
        mapView.setMultiTouchControls(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        mapView.overlays.add(myLocationOverlay)
        progressBar = view.findViewById(R.id.progressBar)
        blurView = view.findViewById(R.id.blur_view)
        loadingText = view.findViewById(R.id.loading_text)
        loadingContainer = view.findViewById(R.id.loading_container)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermission()
        view.findViewById<Button>(R.id.btnClinic).setOnClickListener { showNearbyPlaces("clinic") }
        view.findViewById<Button>(R.id.btnHospital).setOnClickListener { showNearbyPlaces("hospital") }
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
        val repository = mapView.repository
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
            showLoading()
            myLocationOverlay.enableMyLocation()
            myLocationOverlay.runOnFirstFix {
                activity?.runOnUiThread {
                    hideLoading()
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

    private fun showNearbyPlaces(placeType: String) {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val location = myLocationOverlay.myLocation
                if (location == null) {
                    Log.d("Proximity", "Current location is not available.")
                    return
                }
                showLoading()
                val start = GeoPoint(location.latitude, location.longitude)

                thread {
                    val nearbyPlaces = findNearbyPlaces(start, placeType)
                    activity?.runOnUiThread {
                        hideLoading()
                        mapView.overlays.removeAll { it !is MyLocationNewOverlay }
                        if (nearbyPlaces.isNotEmpty()) {
                            for (place in nearbyPlaces) {
                                val marker = createMarker(place, placeType.replaceFirstChar { it.uppercase() }, R.drawable.location_end)
                                marker.setOnMarkerClickListener { marker, _ ->
                                    showRouteToMarker(start, marker.position)
                                    true
                                }
                                mapView.overlays.add(marker)
                            }
                            mapView.invalidate()
                            mapView.controller.setCenter(start)
                            mapView.controller.setZoom(15.0) // Zoom in a bit more after clicking
                        } else {
                            Log.d("Proximity", "No $placeType found nearby.")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Proximity", "Error showing nearby places: ${e.message}", e)
        }
    }

    private fun findNearbyPlaces(currentLocation: GeoPoint, placeType: String): List<GeoPoint> {
        val urlString = "https://nominatim.openstreetmap.org/search?format=json&q=$placeType&viewbox=${currentLocation.longitude-0.1},${currentLocation.latitude+0.1},${currentLocation.longitude+0.1},${currentLocation.latitude-0.1}&bounded=1"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(urlString)
            .build()
        val response: Response

        val places = mutableListOf<GeoPoint>()
        try {
            response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonResponse = response.body!!.string()
            val jsonArray = JSONArray(jsonResponse)
            for (i in 0 until jsonArray.length()) {
                val place = jsonArray.getJSONObject(i)
                val lat = place.getDouble("lat")
                val lon = place.getDouble("lon")
                places.add(GeoPoint(lat, lon))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return places
    }

    private fun createMarker(geoPoint: GeoPoint, title: String, iconResource: Int): Marker {
        return Marker(mapView).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            this.title = title
            icon = ContextCompat.getDrawable(requireContext(), iconResource)
        }
    }

    private fun showRouteToMarker(start: GeoPoint, end: GeoPoint) {
        val roadManager = OSRMRoadManager(requireContext(), USER_AGENT)
        val waypoints = arrayListOf(start, end)

        showLoading()

        thread {
            val road = roadManager.getRoad(waypoints)
            if (road.mStatus != Road.STATUS_OK) {
                Log.e("Proximity", "Error calculating the road: ${road.mStatus}")
            }
            activity?.runOnUiThread {
                hideLoading()
                mapView.overlays.removeAll { it is Polyline }
                val roadOverlay = RoadManager.buildRoadOverlay(road)
                mapView.overlays.add(roadOverlay)
                mapView.invalidate()
            }
        }
    }

    private fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        blurView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingContainer.visibility = View.GONE
        blurView.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }

}
