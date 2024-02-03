package com.example.rapidrescue.ui.SOSMessage

import android.Manifest
import android.content.pm.PackageManager
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
import com.example.rapidrescue.R
import com.google.android.material.card.MaterialCardView

class SOSMessageFragment : Fragment() {

    private val SMS_PERMISSION_CODE = 100
    private lateinit var messageEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_s_o_s_message, container, false)

        messageEditText = view.findViewById(R.id.messageEditText)

        // Set OnClickListener for the "Send Message" button
        view.findViewById<MaterialCardView>(R.id.sendMessageButton).setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString().trim()
        if (message.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                sendSMS(message)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.SEND_SMS),
                    SMS_PERMISSION_CODE
                )
            }
        } else {
            Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSMS(message: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage("PHONE_NUMBER", null, message, null, null)
            Toast.makeText(requireContext(), "Message sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the message
                val message = messageEditText.text.toString().trim()
                sendSMS(message)
            } else {
                Toast.makeText(requireContext(), "Permission denied to send SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }
}