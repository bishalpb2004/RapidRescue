package com.parthiv.rapidrescue.ui.PersonalSafety

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.parthiv.rapidrescue.R
import com.parthiv.rapidrescue.databinding.ActivityPersonalSafetyBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PersonalSafety : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalSafetyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityPersonalSafetyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.personalSafety)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadImages()

        onClicks()
    }

    private fun onClicks() {

        binding.cardViewEarthquake.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.redcross.org/get-help/how-to-prepare-for-emergencies/types-of-emergencies/earthquake.html")
            startActivity(intent)
        }
        binding.cardviewDrought.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.mass.gov/info-details/drought-safety-tips")
            startActivity(intent)
        }
        binding.cardviewFlood.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.ready.gov/floods")
            startActivity(intent)
        }
        binding.cardviewTheft.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://steemit.com/uganda/@agagoe/how-to-survive-theft")
            startActivity(intent)
        }
        binding.cardviewSh.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://everfi.com/blog/workplace-training/strategies-to-prevent-sexual-harassment-at-work/")
            startActivity(intent)
        }

    }

    private fun loadImages() {
        loadImage("cimages/earthquake.jpg", "earthquake.jpg") { bitmap ->
            binding.earthquakeImage.setImageBitmap(bitmap)
        }
        loadImage("cimages/flood.jpg", "flood.jpg") { bitmap ->
            binding.floodImage.setImageBitmap(bitmap)
        }
        loadImage("cimages/drought.jpg", "drought.jpg") { bitmap ->
            binding.droughtImage.setImageBitmap(bitmap)
        }
        loadImage("cimages/theft.jpg", "theft.jpg") { bitmap ->
            binding.theftImage.setImageBitmap(bitmap)
        }
        loadImage("cimages/sh.jpg", "sh.jpg") { bitmap ->
            binding.shImage.setImageBitmap(bitmap)
        }
    }

    private fun loadImage(remotePath: String, localFileName: String, onImageLoaded: (bitmap: Bitmap) -> Unit) {
        val localFile = File(cacheDir, localFileName)

        if (localFile.exists()) {
            // Load the image from the local cache
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            onImageLoaded(bitmap)
        } else {
            // Download the image from Firebase Storage and save it locally
            val storageRef = FirebaseStorage.getInstance().reference.child(remotePath)

            storageRef.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                onImageLoaded(bitmap)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load image: $remotePath", Toast.LENGTH_SHORT).show()
            }
        }
    }

}