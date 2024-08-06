@file:Suppress("PackageName")

package com.google.rapidrescue.ui.SOSMessage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.rapidrescue.R
import com.google.rapidrescue.ui.RegisteredNumbers.SharedViewModel

class SOSMessage : Fragment(), LocationListener {

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    private val SMS_PERMISSION_CODE = 100
    private lateinit var messageEditText: EditText
    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_s_o_s_message, container, false)

        messageEditText = view.findViewById(R.id.messageEditText)
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Set OnClickListener for the "Send Message" button
        view.findViewById<View>(R.id.sendMessageButton).setOnClickListener {
            sendMessage()
        }

        startLocationUpdates()

        return view
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString().trim()
        val selectedPhoneNumber = sharedViewModel.selectedPhoneNumber?.removePrefix("-").orEmpty()

        if (message.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                sendSMSWithLocation(message, selectedPhoneNumber)
                navigateBack()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    SMS_PERMISSION_CODE
                )
            }
        } else {
            Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSMSWithLocation(message: String, phoneNumber: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            val locationLink = currentLocation?.let {
                "https://maps.google.com/?q=${it.latitude},${it.longitude}"
            } ?: "Location not available"

            val finalMessage = "$message\n\nLocation: $locationLink"
            smsManager.sendTextMessage(phoneNumber, null, finalMessage, null, null)
            Toast.makeText(requireContext(), "Message sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                SMS_PERMISSION_CODE
            )
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000L, // Update interval in milliseconds
            10f, // Minimum distance change in meters
            this
        )

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            10000L,
            10f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = location
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Permission denied to access location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_SOSMessageFragment_to_registeredNumbersFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(this)
    }
}
