package com.parthiv.rapidrescue.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.parthiv.rapidrescue.Authentication.User
import com.parthiv.rapidrescue.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController
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
        _binding=FragmentProfileBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        navController=Navigation.findNavController(view)

        binding.loadingOverlay.visibility=View.VISIBLE

        // Ensure the user is authenticated before fetching data
        auth.currentUser?.let { user ->
            readUserData(user.uid)
        } ?: run {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun readUserData(userId: String) {
        databaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        binding.loadingOverlay.visibility=View.GONE
                        user?.let {
                            binding.tvName.text = "Name : ${it.name}"
                            binding.tvSchId.text = "Email : ${it.email}"
                            binding.tvPhone.text = "Phone Number : ${it.phNumber}"
                        }
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Failed to read user data", Toast.LENGTH_LONG).show()
                }
            })
    }
}
