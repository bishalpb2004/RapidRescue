package com.google.rapidrescue.Authentication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.rapidrescue.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var etForgotPass:TextInputEditText
    private lateinit var btnForgotPass:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initi()
        registerEvents()
    }



    private fun initi() {
        mAuth=FirebaseAuth.getInstance()
        etForgotPass=findViewById(R.id.etForgotPassword)
        btnForgotPass=findViewById(R.id.btnForgotPass)
    }

    private fun registerEvents() {
        btnForgotPass.setOnClickListener {
            val sPassword=etForgotPass.text.toString().trim()
            mAuth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(this,"Please Check Your Gmail!",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
        }
    }
}