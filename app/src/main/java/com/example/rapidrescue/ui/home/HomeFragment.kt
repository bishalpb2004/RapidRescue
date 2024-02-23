package com.example.rapidrescue.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {
    private lateinit var navController:NavController
    private var _binding: FragmentHomeBinding? = null
    private lateinit var databaseReference: DatabaseReference
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController=Navigation.findNavController(view)
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.linearLayout5.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.linearLayout3.setOnClickListener{
            navController.navigate(R.id.action_navigation_home_to_registeredNumbersFragment)
        }
        binding.knowInstructions.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_instructionsFragment)
        }

        readData()

    }

    private fun readData() {
        databaseReference.child("Users").get().addOnSuccessListener {

            if (it.exists()){
                for (userSnapshot in it.children) {
                    val name = userSnapshot.child("name").value

                    binding.textviewName.text = "$name"

                }


            }else{

                Toast.makeText(context,"User does not exist", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(context,it.message, Toast.LENGTH_LONG).show()
        }
    }
}