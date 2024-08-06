package com.google.rapidrescue.ui.BugReport

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Spinner
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.rapidrescue.R
import java.io.IOException

class bug_report : AppCompatActivity() {

    private lateinit var bugDescription: EditText
    private lateinit var bugCategorySpinner: Spinner
    private lateinit var takeScreenshotButton: Button
    private lateinit var addVideoButton: Button
    private lateinit var sendBugReportButton: Button
    private lateinit var screenshotPreview: ImageView
    private lateinit var videoPreview: VideoView
    private var screenshotBitmap: Bitmap? = null
    private var screenshotUri: Uri? = null
    private var videoUri: Uri? = null

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
        bugCategorySpinner = findViewById(R.id.bug_category_spinner)
        takeScreenshotButton = findViewById(R.id.take_screenshot_button)
        addVideoButton = findViewById(R.id.add_video_button)
        sendBugReportButton = findViewById(R.id.send_bug_report_button)
        screenshotPreview = findViewById(R.id.screenshot_preview)
        videoPreview = findViewById(R.id.video_preview)

        takeScreenshotButton.setOnClickListener {
            openGallery()
        }

        addVideoButton.setOnClickListener {
            openVideoPicker()
        }

        sendBugReportButton.setOnClickListener {
            sendBugReport()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    private fun openVideoPicker() {
        val videoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(videoIntent, REQUEST_VIDEO_PICK)
    }

    private fun sendBugReport() {
        val description = bugDescription.text.toString()
        val category = bugCategorySpinner.selectedItem.toString()
        val emailAddresses = arrayOf("parthivkumardas@gmail.com", "turinteron@gmail.com", "bishalp.biswas@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, emailAddresses)
            putExtra(Intent.EXTRA_SUBJECT, "Bug Report")
            putExtra(Intent.EXTRA_TEXT, "Category: $category\n\nDescription: $description")

            val uriList = arrayListOf<Uri>()
            screenshotUri?.let { uriList.add(it) }
            videoUri?.let { uriList.add(it) }
            if (uriList.isNotEmpty()) {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            setPackage("com.google.android.gm") // Set Gmail as the email client
        }

        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Gmail app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
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
                REQUEST_VIDEO_PICK -> {
                    data?.data?.let { uri ->
                        videoUri = uri
                        val mediaController = MediaController(this)
                        videoPreview.setMediaController(mediaController)
                        videoPreview.setVideoURI(videoUri)
                        videoPreview.setOnPreparedListener { mediaPlayer ->
                            if (mediaPlayer.duration > MAX_VIDEO_DURATION) {
                                Toast.makeText(this, "Video must be less than 1 minute", Toast.LENGTH_SHORT).show()
                                videoUri = null
                            } else {
                                videoPreview.visibility = VideoView.VISIBLE
                                mediaController.setAnchorView(videoPreview)
                                videoPreview.start()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 2
        private const val REQUEST_VIDEO_PICK = 3
        private const val MAX_VIDEO_DURATION = 60000 // 1 minute in milliseconds
    }
}
