package com.google.rapidrescue.ui.EmergencyContacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.rapidrescue.R
import com.google.android.material.card.MaterialCardView

class EmergencyContacts : AppCompatActivity() {

    private lateinit var phoneNumberToCall: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergen_contacts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fireCardView: MaterialCardView = findViewById(R.id.firecardview)
        val ambulanceCardView: MaterialCardView = findViewById(R.id.ambulancecardview)
        val medicalHelplineCardView: MaterialCardView = findViewById(R.id.medicalhelpcardview)
        val policeCardView: MaterialCardView = findViewById(R.id.policecardview)
        val nationalEmergencyCardView: MaterialCardView = findViewById(R.id.nationalhelpcardview)
        val childHelplineCardView: MaterialCardView = findViewById(R.id.childhelpcardview)
        val womenHelplineCardView: MaterialCardView = findViewById(R.id.womenhelpcardview)

        fireCardView.setOnClickListener { showCallDialog("101") }
        ambulanceCardView.setOnClickListener { showCallDialog("102") }
        medicalHelplineCardView.setOnClickListener { showCallDialog("108") }
        policeCardView.setOnClickListener { showCallDialog("100") }
        nationalEmergencyCardView.setOnClickListener { showCallDialog("112") }
        childHelplineCardView.setOnClickListener { showCallDialog("1098") }
        womenHelplineCardView.setOnClickListener { showCallDialog("1091") }
    }

    private fun showCallDialog(phoneNumber: String) {
        phoneNumberToCall = phoneNumber
        val dialogView = layoutInflater.inflate(R.layout.dialog_call, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.button_call).setOnClickListener {
            makePhoneCall()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
        } else {
            startCall()
        }
    }

    private fun startCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumberToCall"))
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCall()
        }
    }

    companion object {
        private const val REQUEST_CALL_PHONE = 1
    }
}
