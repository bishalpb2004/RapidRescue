package com.parthiv.rapidrescue.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.parthiv.rapidrescue.Authentication.User
import com.parthiv.rapidrescue.R
import com.parthiv.rapidrescue.databinding.FragmentHomeBinding
import com.parthiv.rapidrescue.ui.WeatherSafety.WeatherSafety
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentHomeBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var locationManager: LocationManager
    private var lastBackPressedTime: Long = 0
    private val backPressThreshold: Long = 2000

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.emergencyDel
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherSafetyCard: CardView = view.findViewById(R.id.weathercard)

        weatherSafetyCard.setOnClickListener {
            val intent = Intent(activity, WeatherSafety::class.java)
            startActivity(intent)


        }

        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        databaseReference.keepSynced(true)

        disableClickableElements()

        binding.loadingOverlay.visibility = View.VISIBLE

        auth.currentUser?.let {
            readUserData(it.uid)
        } ?: run {
            Toast.makeText(context, "User Not Authenticated", Toast.LENGTH_SHORT).show()
        }

        binding.panicButtonCard.setOnClickListener {
            showConfirmationDialog()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Send Emergency SMS")
            .setMessage("Are you sure you want to send emergency messages to all registered numbers?")
            .setPositiveButton("Yes") { _, _ ->
                sendMessage()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun sendMessage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getCurrentLocationAndSendSms()
        }
    }

    private fun getCurrentLocationAndSendSms() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationManager.removeUpdates(this)
                fetchRegisteredNumbersAndSendSms(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    private fun fetchRegisteredNumbersAndSendSms(location: Location) {
        val userId = auth.currentUser?.uid ?: return
        val numbersRef = FirebaseDatabase.getInstance().getReference("Name & Number").child(userId)

        numbersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val phoneNumbers = mutableListOf<String>()
                for (numberSnapshot in snapshot.children) {
                    val phoneNumber = numberSnapshot.child("phoneNumber").getValue(String::class.java)
                    phoneNumber?.let { phoneNumbers.add(it) }
                }
                if (phoneNumbers.isNotEmpty()) {
                    sendSmsToNumbers(phoneNumbers, location)
                } else {
                    Toast.makeText(context, "No registered numbers found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to fetch numbers: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendSmsToNumbers(phoneNumbers: List<String>, location: Location) {
        val smsManager = SmsManager.getDefault()
        val message = "Help!\n My current location is: https://maps.google.com/?q=${location.latitude},${location.longitude}"

        for (number in phoneNumbers) {
            smsManager.sendTextMessage(number, null, message, null, null)
        }
        Toast.makeText(context, "Emergency messages sent", Toast.LENGTH_SHORT).show()
    }

    private fun showExitDialog() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressedTime < backPressThreshold) {
            requireActivity().finishAffinity()
        } else {
            lastBackPressedTime = currentTime
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readUserData(userId: String) {
        databaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        binding.loadingOverlay.visibility = View.GONE
                        user?.let {
                            binding.textviewName.text = it.name
                        }
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_LONG).show()
                    }
                    enableClickableElements()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Failed to read user data", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun disableClickableElements() {
        binding.viewEmergencyCard.isClickable = false
        binding.viewEmergencyCard.isClickable = false
        binding.instructionsCard.isClickable = false
        binding.instructionsCard.setOnClickListener(null)
        binding.viewEmergencyCard.setOnClickListener(null)
        binding.instructionsCard.setOnClickListener(null)
    }

    private fun enableClickableElements() {
        binding.instructionsCard.isClickable = true
        binding.viewEmergencyCard.isClickable = true
        binding.instructionsCard.isClickable = true
        binding.instructionsCard.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.viewEmergencyCard.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.instructionsCard.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_instructionsFragment)
        }
    }
}
