package com.google.rapidrescue.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.rapidrescue.MainActivity
import com.google.rapidrescue.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef:DatabaseReference
    private lateinit var etEmail:TextInputEditText
    private lateinit var etPass:TextInputEditText
    private lateinit var btnLogin:Button
    private lateinit var tvLogin:TextView
    private lateinit var tvForgotPass:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        initi()
        registerEvents()
    }

    private fun initi() {
        mAuth=FirebaseAuth.getInstance()
        dbRef=FirebaseDatabase.getInstance()
            .reference.child("Users")
            .child(mAuth.currentUser?.uid.toString())
        etEmail=findViewById(R.id.etEmailLoginPage)
        etPass=findViewById(R.id.etPassLoginPage)
        btnLogin=findViewById(R.id.btnLogin1)
        tvLogin=findViewById(R.id.tvLoginPage)
        tvForgotPass=findViewById(R.id.tvForgotPass)
    }

    private fun registerEvents() {
        tvLogin.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        tvForgotPass.setOnClickListener {
            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }
        btnLogin.setOnClickListener {
            val email=etEmail.text.toString().trim()
            val pass=etPass.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val verification=mAuth.currentUser?.isEmailVerified
                            when(verification){
                                true->{
                                    Toast.makeText(this,"Login Successfully",Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    //finish() // Finish LoginActivity to prevent going back to it
                                }
                                else-> Toast.makeText(this,"Please check your Gmail and Verify your email id",Toast.LENGTH_LONG).show()
                            }
                        }
                        else{
                            Toast.makeText(this,
                                "Kindly Sign Up by clicking SignUp Button",
                                Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }else{
                Toast.makeText(this,"Please fill up all the necessary details",Toast.LENGTH_LONG).show()
            }
        }
    }
}
