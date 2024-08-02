package com.example.rapidrescue.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rapidrescue.databinding.FragmentFetchUserDataBinding
import com.example.rapidrescue.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FetchUserDataFragment : Fragment() {

    private lateinit var binding: FragmentFetchUserDataBinding
    private lateinit var navController: NavController
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentFetchUserDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()
        navController=Navigation.findNavController(view)
        databaseReference = FirebaseDatabase.getInstance().reference

//        databaseReference.addValueEventListener(object :ValueEventListener{
//
//        })

        this.readData()

    }

    @SuppressLint("SetTextI18n")
    private fun readData() {

        databaseReference.ref.child("Users")
            .child(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener {

            if (it.exists()){
                for (userSnapshot in it.children) {
                    val name = userSnapshot.child("name").value
                    val email = userSnapshot.child("email").value
                    val phNumber = userSnapshot.child("phNumber").value

                    binding.tvName1.text = "Name : $name"
                    binding.tvSchId1.text = "Scholar ID : $email"
                    binding.tvPhone1.text = "Phone Number : $phNumber"
                }

            }else{

                Toast.makeText(context,"User does not exist", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }

    }

}