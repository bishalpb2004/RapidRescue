package com.example.rapidrescue.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentHomeBinding
import com.example.rapidrescue.ui.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var navController:NavController
    private var _binding: FragmentHomeBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        auth= FirebaseAuth.getInstance()
        navController=Navigation.findNavController(view)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        disableClickableElements()

        binding.loadingOverlay.visibility = View.VISIBLE

//        readData()

        auth.currentUser?.let {
            readUserData(it.uid)
        }?:run {
            Toast.makeText(context,"User Not Authenticated",Toast.LENGTH_SHORT).show()
        }

         val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            // Do nothing or handle as needed
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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


//    private fun readData() {
//        databaseReference.ref.child("Users")
//            .child(auth.currentUser?.uid.toString())
//            .get().addOnCompleteListener { task ->
//            // Hide loading overlay regardless of success or failure
//            binding.loadingOverlay.visibility = View.GONE
//
//            if (task.isSuccessful) {
//                val snapshot = task.result
//                if (snapshot != null && snapshot.exists()) {
//                    for (userSnapshot in snapshot.children) {
//                        val name = userSnapshot.child("name").value
//                        binding.textviewName.text = "$name"
//                    }
//                } else {
//                    Toast.makeText(context, "User does not exist", Toast.LENGTH_LONG).show()
//                }
//            } else {
//                // Handle failure case
//                Toast.makeText(context, task.exception?.message ?: "Failed to fetch data", Toast.LENGTH_LONG).show()
//            }
//            // Enable clickable elements after data loading completes
//            enableClickableElements()
//        }
//    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun disableClickableElements() {
        // Disable any clickable elements or set onClick listeners to null
        binding.linearLayout5.isClickable = false
        binding.linearLayout3.isClickable = false
        binding.knowInstructions.isClickable = false
        binding.linearLayout5.setOnClickListener(null)
        binding.linearLayout3.setOnClickListener(null)
        binding.knowInstructions.setOnClickListener(null)
    }

    private fun enableClickableElements() {
        // Enable clickable elements and set onClick listeners back
        binding.linearLayout5.isClickable = true
        binding.linearLayout3.isClickable = true
        binding.knowInstructions.isClickable = true
        binding.linearLayout5.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.linearLayout3.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.knowInstructions.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_instructionsFragment)
        }


    }
}