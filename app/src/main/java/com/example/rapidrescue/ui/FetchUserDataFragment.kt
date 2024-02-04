package com.example.rapidrescue.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentFetchUserDataBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FetchUserDataFragment : Fragment() {

    private lateinit var binding: FragmentFetchUserDataBinding
    private lateinit var navController: NavController
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFetchUserDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        databaseReference = FirebaseDatabase.getInstance().getReference()

//        databaseReference.addValueEventListener(object :ValueEventListener{
//
//        })

            readData()



    }

    @SuppressLint("SetTextI18n")
    private fun readData() {


        databaseReference.child("Users").get().addOnSuccessListener {

            if (it.exists()){
                for (userSnapshot in it.children) {
                    val name = userSnapshot.child("name").value
                    val schId = userSnapshot.child("schNumber").value
                    val phNumber = userSnapshot.child("phoneNumber").value

                    binding.tvName.text = "NAME : $name"
                    binding.tvSchId.text = "SCHOLAR ID : $schId"
                    binding.tvPhone.text = "PHONE NUMBER : $phNumber"
                }


            }else{

                Toast.makeText(context,"User does not exist", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }



    }

}