package com.example.rapidrescue.ui.RegisteredNumbers

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentRegisteredNumbersBinding
import com.example.rapidrescue.ui.PopUpFragment
import com.example.rapidrescue.ui.add.AddAdapter
import com.example.rapidrescue.ui.add.AddDataModel
import com.example.rapidrescue.ui.notifications.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase.*
import com.google.firebase.database.ValueEventListener

class RegisteredNumbersFragment : Fragment(), PopUpFragment.DialogNextBtnClickListener,
    AddAdapter.AddAdapterClicksInterface {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference
    private lateinit var adapter:AddAdapter
    private lateinit var popUpFragment: PopUpFragment
    private lateinit var mList:MutableList<AddDataModel>
    private lateinit var navController: NavController
    private var _binding: FragmentRegisteredNumbersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentRegisteredNumbersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
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

        init(view)
        getDataFromFirebase()
        registerEvents()

    }

    private fun registerEvents() {
        binding.addBtnRegister.setOnClickListener {
            popUpFragment= PopUpFragment()
            popUpFragment.setListener(this)
            popUpFragment.show(
                childFragmentManager,
                "PopUpFragment"
            )
        }
    }

    private fun getDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (numberSnapshot in snapshot.children) {
                    val name = numberSnapshot.child("name").getValue(String::class.java) ?: ""
                    val phoneNumber = numberSnapshot.child("phoneNumber").getValue(String::class.java) ?: ""

                    val registeredNumber = AddDataModel(name, phoneNumber)
                    mList.add(registeredNumber)
                }

                val binding = _binding // Local variable to store the reference
                if (binding != null) { // Check if the binding is not null
                    val adapter = AddAdapter(mList)
                    binding.recyclerView.adapter = adapter
                    adapter.setListener(object : AddAdapter.AddAdapterClicksInterface {
                        override fun onDeleteNumberBtnClicked(addNumberData: AddDataModel) {
                            // Query to find the specific number
                            databaseReference
                                .orderByChild("name")
                                .equalTo(addNumberData.name)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (numberSnapshot in snapshot.children) {
                                            // Check if the found entry matches the data to be deleted
                                            if (numberSnapshot.child("phoneNumber").getValue(String::class.java) == addNumberData.phoneNumber &&
                                                numberSnapshot.child("name").getValue(String::class.java) == addNumberData.name) {
                                                // Delete the entry
                                                numberSnapshot.ref.removeValue()
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(context, "Deletion failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                                    }
                                })
                        }


                        override fun onEditNumberBtnClicked(addNumberData: AddDataModel) {
                            // Your edit logic here
                        }

                        override fun onItemClick(position: Int) {
                            val selectedNumber = mList[position].phoneNumber
                            val selectedName = mList[position].name

                            sharedViewModel.selectedPhoneNumber = selectedNumber

                            // Assuming you want to navigate based on the selected name
                            databaseReference.orderByChild("name").equalTo(selectedName)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            // Navigate to the appropriate fragment based on the selected name
                                            navController.navigate(R.id.action_registeredNumbersFragment_to_SOSMessageFragment)
                                        } else {
                                            Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                                    }
                                })
                        }

                    })
                    requireActivity().runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }




    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseReference= getInstance().reference.child("Name & Number")
            .child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        mList= mutableListOf()
        adapter=AddAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter=adapter
    }

    override fun onSaveTask(name: String, phoneNumber: String, phoneNumberEt: EditText) {
        databaseReference.push().setValue(AddDataModel(name, phoneNumber))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show()
                    phoneNumberEt.text = null
                } else {
                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
                popUpFragment.dismiss()
            }
    }


    override fun onDeleteNumberBtnClicked(addNumberData: AddDataModel) {
//        databaseReference.child(addNumberData.name).removeValue().addOnCompleteListener {
//            if (it.isSuccessful){
//                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
//
//            }
//            else{
//
//                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
//
//            }
//        }
    }

    override fun onEditNumberBtnClicked(addNumberData: AddDataModel) {

    }

    override fun onItemClick(position: Int) {
//        navController.navigate(R.id.action_registeredNumbersFragment_to_SOSMessageFragment)
    }

}