package com.example.rapidrescue.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentAfterProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AfterProfileFragment : Fragment() {

    private lateinit var binding:FragmentAfterProfileBinding
    private lateinit var databaseRef:DatabaseReference
    private lateinit var navController: NavController
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentAfterProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()
        navController=Navigation.findNavController(view)

        val name=binding.nameEtProfile.text.toString()
        val schId=binding.scholarNumberProfile.text.toString()
        val phNumber=binding.phoneNumberProfile.text.toString()

        val user=User(name,schId,phNumber)

        databaseRef=FirebaseDatabase.getInstance()
            .reference.child("Users")
            .child(auth.currentUser?.uid.toString())

        binding.btnSaveProfile.setOnClickListener {
            uploadData()
        }
    }

    private fun uploadData() {

        val name=binding.nameEtProfile.text.toString().trim()
        val schId=binding.scholarNumberProfile.text.toString().trim()
        val phNumber=binding.phoneNumberProfile.text.toString().trim()

        if (name.isNotEmpty() && schId.isNotEmpty() &&  phNumber.length==10){
            val entryKey=databaseRef.push().key

            val userModel=User(name,schId,phNumber)

            entryKey?.let {
                databaseRef.child(it).setValue(userModel)
                    .addOnCompleteListener {

                        Toast.makeText(context,"User added successfully",Toast.LENGTH_LONG).show()


                }.addOnFailureListener {
                        Toast.makeText(context,"User could not be added ",Toast.LENGTH_LONG).show()

                }
            }
            binding.nameEtProfile.text?.clear()
            binding.scholarNumberProfile.text?.clear()
            binding.phoneNumberProfile.text?.clear()

            navigateToNextFragment()
            makeButtonInvisible()

        }else if (name.isEmpty()){
            Toast.makeText(context,"Fill up your name",Toast.LENGTH_SHORT).show()

        }
        else if (schId.isEmpty()){
            Toast.makeText(context,"Fill up your scholar id",Toast.LENGTH_SHORT).show()
        }
        else if (phNumber.length!=10) {
            Toast.makeText(context,"Enter a valid phone number",Toast.LENGTH_SHORT).show()

        }

    }


    private fun makeButtonInvisible() {
        binding.btnSaveProfile.visibility=View.GONE
    }

    private fun navigateToNextFragment() {
        findNavController().navigate(R.id.action_afterProfileFragment_to_navigation_notifications)
    }


}