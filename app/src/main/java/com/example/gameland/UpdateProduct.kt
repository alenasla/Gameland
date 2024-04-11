package com.example.gameland

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import androidx.activity.result.contract.ActivityResultContracts

class UpdateProduct : AppCompatActivity() {
    private lateinit var updateImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var updateName: EditText
    private lateinit var updateCategory: EditText
    private lateinit var updatePrice: EditText
    private lateinit var updateDescription: EditText
    private var title: String? = null
    private var description: String? = null
    private var category: String? = null
    private var price: String? = null
    private var imageUrl: String? = null
    private var key: String? = null
    private var oldImageURL: String? = null
    private var uri: Uri? = null
    private lateinit var databaseReference: DatabaseReference
    private var storageReference: StorageReference? = null

    // Register the activity result launcher
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            this.uri = uri
            updateImage.setImageURI(uri)
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Initialize views
        updateButton = findViewById(R.id.updateButton)
        updateDescription = findViewById(R.id.updateDescription)
        updateImage = findViewById(R.id.updateImage)
        updatePrice = findViewById(R.id.updatePrice)
        updateCategory = findViewById(R.id.updateCategory)
        updateName = findViewById(R.id.updateName)

        intent.extras?.let { bundle ->
            Glide.with(this).load(bundle.getString("Image")).into(updateImage)
            updateName.setText(bundle.getString("Title"))
            updateDescription.setText(bundle.getString("Description"))
            updateCategory.setText(bundle.getString("Category"))
            updatePrice.setText(bundle.getString("Price"))
            key = bundle.getString("Key")
            oldImageURL = bundle.getString("Image")
        }

        // Initialize Firebase reference
        key?.let {
            databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(it)
        }

        updateImage.setOnClickListener {
            getImage.launch("image/*")
        }

        updateButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        uri?.let { uri ->
            val dialog = createProgressDialog()
            dialog.show()
            storageReference = FirebaseStorage.getInstance().getReference("Android Images").child(uri.lastPathSegment!!)
            storageReference!!.putFile(uri).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    updateData()
                    dialog.dismiss()
                }
            }.addOnFailureListener { e ->
                dialog.dismiss()
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            imageUrl = oldImageURL
            updateData()
        }
    }

    private fun updateData() {
        title = updateName.text.toString().trim()
        description = updateDescription.text.toString().trim()
        category = updateCategory.text.toString().trim()
        price = updatePrice.text.toString().trim()

        if (title.isNullOrEmpty() || description.isNullOrEmpty() || category.isNullOrEmpty() || price.isNullOrEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        val product = DataClass(title, description, category, price, imageUrl).also { dataClass ->
            oldImageURL?.takeIf { it.isNotEmpty() && it != imageUrl }?.let { oldUrl ->
                FirebaseStorage.getInstance().getReferenceFromUrl(oldUrl).delete()
            }
        }

        databaseReference.setValue(product).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Update failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createProgressDialog(): AlertDialog {
        return AlertDialog.Builder(this).apply {
            setCancelable(false)
            setView(R.layout.progress_layout)
        }.create()
    }
}
