package com.example.rapidrescue.ui.proximity


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rapidrescue.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class proximity : Fragment() {

    private lateinit var mgoogleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proximity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            mgoogleMap = googleMap
            requestLocationPermission()
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
            mgoogleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
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

//package com.example.rapidrescue.ui.proximity
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.rapidrescue.R
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.FetchPlaceRequest
//import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
//import com.google.android.libraries.places.api.net.PlacesClient
//
//class proximity : Fragment() {
//
//    private lateinit var mGoogleMap: GoogleMap
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val LOCATION_PERMISSION_REQUEST_CODE = 1
//    private lateinit var placesClient: PlacesClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        Places.initialize(requireContext(), getString(R.string.google_maps_key))
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_proximity, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        placesClient = Places.createClient(requireContext())
//
//        view.findViewById<Button>(R.id.btnPolice).setOnClickListener {
//            fetchNearbyPlaces(Place.Type.POLICE)
//        }
//
//        view.findViewById<Button>(R.id.btnHospital).setOnClickListener {
//            fetchNearbyPlaces(Place.Type.HOSPITAL)
//        }
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as? SupportMapFragment
//        mapFragment?.getMapAsync { googleMap ->
//            mGoogleMap = googleMap
//            requestLocationPermission()
//        }
//    }
//
//    private fun requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            // Permission has already been granted
//            enableMyLocation()
//        }
//    }
//
//    private fun enableMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            try {
//                mGoogleMap.isMyLocationEnabled = true
//                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                    location?.let {
//                        val currentLatLng = LatLng(it.latitude, it.longitude)
//                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                    }
//                }
//            } catch (e: SecurityException) {
//                // Handle the exception
//                e.printStackTrace()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                enableMyLocation()
//            }
//        }
//    }
//
//    private fun fetchNearbyPlaces(placeType: Place.Type) {
//        val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
//
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                location?.let {
//                    val currentLatLng = LatLng(it.latitude, it.longitude)
//
//                    // Create a FindCurrentPlaceRequest with required place fields
//                    val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//                    placesClient.findCurrentPlace(request)
//                        .addOnSuccessListener { response ->
//                            mGoogleMap.clear()
//                            for (placeLikelihood in response.placeLikelihoods) {
//                                val place = placeLikelihood.place
//                                if (place.types?.contains(placeType) == true) {
//                                    mGoogleMap.addMarker(
//                                        MarkerOptions().position(place.latLng!!).title(place.name)
//                                    )
//                                }
//                            }
//                            // Move the camera to the user's location
//                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                        }
//                        .addOnFailureListener { exception ->
//                            // Handle the error.
//                            exception.printStackTrace()
//                        }
//                }
//            }
//        }
//    }
//
//
//    private fun fetchPlaceDetails(place: Place, placeFields: List<Place.Field>) {
//        val request = FetchPlaceRequest.builder(place.id!!, placeFields).build()
//
//        placesClient.fetchPlace(request)
//            .addOnSuccessListener { response ->
//                val place = response.place
//                displayPlaceOnMap(place)
//            }
//            .addOnFailureListener { exception ->
//                // Handle the error.
//                exception.printStackTrace()
//            }
//    }
//
//    private fun displayPlaceOnMap(place: Place) {
//        // Add the place marker to the map
//        val latLng = place.latLng
//        mGoogleMap.addMarker(MarkerOptions().position(latLng!!).title(place.name))
//    }
//}

