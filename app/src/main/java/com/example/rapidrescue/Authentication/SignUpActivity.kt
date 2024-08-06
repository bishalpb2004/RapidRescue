package com.example.rapidrescue.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidrescue.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var etEmail:TextInputEditText
    private lateinit var etName:TextInputEditText
    private lateinit var etPass:TextInputEditText
    private lateinit var rePass:TextInputEditText
    private lateinit var etPhNumber:TextInputEditText
    private lateinit var btnSignUp:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initi()
        registerEvents()
    }
    private fun initi() {
        btnSignUp=findViewById(R.id.btnSignUp)
        etPass=findViewById(R.id.etPassSignUpPage)
        etPhNumber=findViewById(R.id.etPhNumber)
        etName=findViewById(R.id.etNameSignUpPage)
        etEmail=findViewById(R.id.etEmailSignUpPage)
        rePass=findViewById(R.id.etRePassSignUpPage)
        mAuth=FirebaseAuth.getInstance()
        dbReference=FirebaseDatabase.getInstance()
            .reference.child("Users")
    }

    private fun registerEvents() {
        btnSignUp.setOnClickListener {
            val email=etEmail.text.toString().trim()
            val phNumber=etPhNumber.text.toString().trim()
            val pass=etPass.text.toString().trim()
            val name=etName.text.toString().trim()
            val rePass=rePass.text.toString().trim()

            if (email.isNotEmpty() && phNumber.isNotEmpty()
                && pass.isNotEmpty() && name.isNotEmpty() && rePass.isNotEmpty()){

                if (pass==rePass){
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            val currentUser=mAuth.currentUser
                            val userId=currentUser?.uid

                            val userModel=User(name,email,phNumber,pass)
                            userId?.let {
                                dbReference.child(it).setValue(userModel)
                                    .addOnCompleteListener {
                                        if(it.isSuccessful){
                                            currentUser.sendEmailVerification().addOnSuccessListener {
                                                Toast.makeText(this,"Please check your Gmail to verify your email id!",Toast.LENGTH_LONG).show()
                                                startActivity(Intent(this,LoginActivity::class.java))
                                            }
                                                .addOnFailureListener {
                                                    Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                                                }
                                        }
                                        else{
                                            Toast.makeText(this,"User could not be added",Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        }else{
                            val verification = mAuth.currentUser?.isEmailVerified
                            if(verification == false)
                                Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }

            }else {
                Toast.makeText(this, "Please fill up all the necessary details", Toast.LENGTH_SHORT).show()
            }

        }
    }

}
