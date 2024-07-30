package com.example.rapidrescue.Authentication

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidrescue.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val name = binding.etNameSignUpPage.text.toString().trim()
            val email = binding.etEmailSignUpPage.text.toString().trim()
            val password = binding.etPassSignUpPage.text.toString().trim()
            val rePassword = binding.etRePassSignUpPage.text.toString().trim()

            if (validateInput(name, email, password, rePassword)) {
                registerUser(email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String, rePassword: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                binding.etNameSignUpPage.error = "Name is required"
                false
            }
            TextUtils.isEmpty(email) -> {
                binding.etEmailSignUpPage.error = "Email is required"
                false
            }
            TextUtils.isEmpty(password) -> {
                binding.etPassSignUpPage.error = "Password is required"
                false
            }
            TextUtils.isEmpty(rePassword) -> {
                binding.etRePassSignUpPage.error = "Please re-enter your password"
                false
            }
            password != rePassword -> {
                binding.etRePassSignUpPage.error = "Passwords do not match"
                false
            }
            else -> true
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        sendVerificationEmail(it)
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                    // Optionally, you can sign out the user after sending the verification email
                    auth.signOut()
                } else {
                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}