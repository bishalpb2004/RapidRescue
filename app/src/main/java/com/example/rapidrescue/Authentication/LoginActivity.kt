package com.example.rapidrescue.Authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidrescue.MainActivity
import com.example.rapidrescue.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLoginPage.text.toString().trim()
            val password = binding.etPassLoginPage.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvLoginPage.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPass.setOnClickListener {
            // Implement forgot password functionality
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                binding.etEmailLoginPage.error = "Email is required"
                false
            }
            TextUtils.isEmpty(password) -> {
                binding.etPassLoginPage.error = "Password is required"
                false
            }
            else -> true
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Navigate to main activity or home screen
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Please verify your email address", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
