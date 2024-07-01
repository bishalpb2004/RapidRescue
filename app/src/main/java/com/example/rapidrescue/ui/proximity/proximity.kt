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
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road


class Proximity : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val DEFAULT_ZOOM_LEVEL = 6.0 // Adjust the zoom level as needed
    private val USER_AGENT = "YOUR_APP_NAME" // Replace with your app name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize OSM configuration (mandatory)
        val osmConfig = Configuration.getInstance()
        osmConfig.load(requireContext(), requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_proximity, container, false)

        // Initialize map view
        mapView = view.findViewById(R.id.map_view)
        mapView.setMultiTouchControls(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Set the tile source here

        // Initialize my location overlay
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        mapView.overlays.add(myLocationOverlay)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLocationPermission()

        // Set default zoom level and center on the map when permissions are granted
        mapView.controller.setZoom(DEFAULT_ZOOM_LEVEL)
        mapView.controller.animateTo(myLocationOverlay.myLocation)

        view.findViewById<Button>(R.id.btnPolice).setOnClickListener {
            Log.d("Proximity", "Police button clicked")
            showShortestPath("police")
        }

        view.findViewById<Button>(R.id.btnHospital).setOnClickListener {
            Log.d("Proximity", "Hospital button clicked")
            showShortestPath("hospital")
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has already been granted
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myLocationOverlay.enableMyLocation()
            mapView.controller.animateTo(myLocationOverlay.myLocation)
        }
    }

    private fun showShortestPath(placeType: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = myLocationOverlay.myLocation
            location?.let {
                // Create a RoadManager object with user agent
                val roadManager = OSRMRoadManager(requireContext(), USER_AGENT)

                // Create start and end GeoPoints
                val start = GeoPoint(it.latitude, it.longitude)
                val end = findNearestPlace(location, placeType)

                // Check if we found a valid end point
                if (end != null) {
                    // Fetch the road between start and end points
                    val road = roadManager.getRoad(
                        arrayListOf(start, end)
                    )

                    // Clear previous overlays (if any)
                    mapView.overlays.removeAll { it !is MyLocationNewOverlay }

                    // Add the road overlay to the map
                    val roadOverlay = RoadManager.buildRoadOverlay(road)
                    mapView.overlays.add(roadOverlay)

                    // Add markers for start and end points
                    mapView.overlays.add(createMarker(start, "Start", R.drawable.ic_cloud))
                    mapView.overlays.add(createMarker(end, placeType.capitalize(), R.drawable.new_panic))

                    // Refresh the map to display the changes
                    mapView.invalidate()

                    // Zoom and center the map on the road
                    mapView.controller.setCenter(start)
                    mapView.controller.zoomTo(15.0)
                } else {
                    Log.d("Proximity", "No $placeType found nearby.")
                }
            }
        }
    }

    private fun findNearestPlace(currentLocation: GeoPoint, placeType: String): GeoPoint? {
        // Implement your logic to find the nearest place of the specified type
        // This is a placeholder method, replace with your actual implementation
        return null
    }

    private fun createMarker(geoPoint: GeoPoint, title: String, iconResource: Int): Marker {
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = title
        marker.icon = ContextCompat.getDrawable(requireContext(), iconResource)
        return marker
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }
}









