package com.example.gameland

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.DateFormat
import java.util.Calendar

class UploadProduct : AppCompatActivity() {
    private lateinit var uploadImage: ImageView
    private lateinit var saveButton: Button
    private lateinit var uploadName: EditText
    private lateinit var uploadCategory: EditText
    private lateinit var uploadDescription: EditText
    private lateinit var uploadPrice: EditText
    private var imageURL: String? = null
    private var uri: Uri? = null

    // Using the new Activity Result API to handle content selection
    private val contentActivityResult = registerForActivityResult(ActivityResultContracts.GetContent()) { resultUri: Uri? ->
        if (resultUri != null) {
            uri = resultUri
            uploadImage.setImageURI(uri)
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_product)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        uploadImage = findViewById(R.id.uploadImage)
        saveButton = findViewById(R.id.saveButton)
        uploadName = findViewById(R.id.uploadName)
        uploadCategory = findViewById(R.id.uploadCategory)
        uploadDescription = findViewById(R.id.uploadDescription)
        uploadPrice = findViewById(R.id.uploadPrice)

        uploadImage.setOnClickListener {
            // Launching the image picker
            contentActivityResult.launch("image/*")
        }

        saveButton.setOnClickListener { saveData() }
    }

    private fun saveData() {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.progress_layout)
            .create()
        dialog.show()

        uri?.let { selectedUri ->
            val storagePath = "Android Images/${selectedUri.lastPathSegment}"
            val storageReference = FirebaseStorage.getInstance().getReference(storagePath)

            storageReference.putFile(selectedUri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageURL = downloadUri.toString()
                    uploadData()
                }
            }.addOnCompleteListener {
                dialog.dismiss()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        } ?: run {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun uploadData() {
        val name = uploadName.text.toString().trim()
        val category = uploadCategory.text.toString().trim()
        val description = uploadDescription.text.toString().trim()
        val price = uploadPrice.text.toString().trim()

        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || price.isEmpty() || imageURL == null) {
            Toast.makeText(this, "Please fill in all fields and upload an image", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        val productRef = FirebaseDatabase.getInstance().getReference("Products").child(currentDate)
        val product = DataClass(name, category, description, price, imageURL)

        productRef.setValue(product).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save product: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}