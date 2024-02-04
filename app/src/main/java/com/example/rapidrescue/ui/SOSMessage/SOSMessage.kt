package com.example.rapidrescue.ui.SOSMessage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
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
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.RegisteredNumbers.SharedViewModel

class SOSMessage : Fragment() {

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private val SMS_PERMISSION_CODE = 100
    private lateinit var messageEditText: EditText
    private lateinit var phoneNumberEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_s_o_s_message, container, false)

        messageEditText = view.findViewById(R.id.messageEditText)


        // Set OnClickListener for the "Send Message" button
        view.findViewById<View>(R.id.sendMessageButton).setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString().trim()
        val _selectedPhoneNumber = sharedViewModel.selectedPhoneNumber.toString()
        val selectedPhoneNumber1 = _selectedPhoneNumber.removeRange(0,13)
        val selectedPhoneNumber = selectedPhoneNumber1.removeRange(10, selectedPhoneNumber1.length)
        if (message.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
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
            val location = getCurrentLocation()
            val finalMessage = "$message\n\nLocation: $location"
            smsManager.sendTextMessage(phoneNumber, null, finalMessage, null, null)
            Toast.makeText(requireContext(), "Message sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    private fun getCurrentLocation(): String {
        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get the user's current location
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // Check if location is available
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                return "Latitude: $latitude, Longitude: $longitude"
            }
        }

        return "Location not available"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permissions granted, send the message with location
                val message = messageEditText.text.toString().trim()
                val selectedNumber = sharedViewModel.selectedPhoneNumber.toString()

                    sendSMSWithLocation(message, selectedNumber)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied to send SMS or access location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateBack(){
        findNavController().navigate(R.id.action_SOSMessageFragment_to_registeredNumbersFragment)
    }
}
