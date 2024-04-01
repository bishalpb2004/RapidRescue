package com.example.rapidrescue

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import com.example.rapidrescue.databinding.FragmentAfterProfileBinding
import com.example.rapidrescue.ui.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var saveBtn:Button
    private lateinit var nameEt:TextInputEditText
    private lateinit var schIdEt:TextInputEditText
    private lateinit var phoneNumberEt:TextInputEditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        saveBtn=findViewById(R.id.btnSaveProfile)
        nameEt=findViewById(R.id.nameEtProfile)
        schIdEt=findViewById(R.id.scholarNumberProfile)
        phoneNumberEt=findViewById(R.id.phoneNumberProfile)

        auth= FirebaseAuth.getInstance()
        databaseRef= FirebaseDatabase.getInstance()
            .reference.child("Users")

        if (auth.currentUser!=null){
            sendToMain()
        }

        saveBtn.setOnClickListener {
            uploadData()
        }

    }

    private fun uploadData() {
        val name=nameEt.text.toString().trim()
        val schId=schIdEt.text.toString().trim()
        val phNumber=phoneNumberEt.text.toString().trim()

        if (name.isNotEmpty() && schId.isNotEmpty() &&  phNumber.length==10){
            val entryKey=databaseRef.push().key

            val currentUser = auth.currentUser
            val userId = currentUser?.uid

            val userModel= User(name,schId,phNumber)

            userId?.let {
                databaseRef.child(it).setValue(userModel)
                    .addOnCompleteListener {

                        Toast.makeText(this,"User added successfully", Toast.LENGTH_LONG).show()
                        sendToMain()

                    }.addOnFailureListener {
                        Toast.makeText(this,"User could not be added ", Toast.LENGTH_LONG).show()

                    }
            }
            nameEt.text?.clear()
            schIdEt.text?.clear()
            phoneNumberEt.text?.clear()

        }else if (name.isEmpty()){
            Toast.makeText(this,"Fill up your name", Toast.LENGTH_SHORT).show()

        }
        else if (schId.isEmpty()){
            Toast.makeText(this,"Fill up your scholar id", Toast.LENGTH_SHORT).show()
        }
        else if (phNumber.length!=10) {
            Toast.makeText(this,"Enter a valid phone number", Toast.LENGTH_SHORT).show()

        }
    }

    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}