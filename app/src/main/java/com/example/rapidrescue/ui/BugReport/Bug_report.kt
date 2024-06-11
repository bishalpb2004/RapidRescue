package com.example.rapidrescue.ui.BugReport

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rapidrescue.R
import java.io.IOException

class bug_report : AppCompatActivity() {

    private lateinit var bugDescription: EditText
    private lateinit var takeScreenshotButton: Button
    private lateinit var sendBugReportButton: Button
    private lateinit var screenshotPreview: ImageView

    private var screenshotBitmap: Bitmap? = null
    private var screenshotUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_bug_report)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        bugDescription = findViewById(R.id.bug_description)
        takeScreenshotButton = findViewById(R.id.take_screenshot_button)
        sendBugReportButton = findViewById(R.id.send_bug_report_button)
        screenshotPreview = findViewById(R.id.screenshot_preview)

        takeScreenshotButton.setOnClickListener {
            openGallery()
        }

        sendBugReportButton.setOnClickListener {
            sendBugReport()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    private fun sendBugReport() {
        val description = bugDescription.text.toString()
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@example.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Bug Report")
            putExtra(Intent.EXTRA_TEXT, description)

            screenshotUri?.let {
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        startActivity(Intent.createChooser(emailIntent, "Send Bug Report"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                screenshotUri = uri
                try {
                    val selectedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, screenshotUri)
                    screenshotBitmap = selectedImageBitmap
                    screenshotPreview.setImageBitmap(screenshotBitmap)
                    screenshotPreview.visibility = ImageView.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 2
    }
}

