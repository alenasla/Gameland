package com.example.gameland

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailActivity : AppCompatActivity() {
    private var detailDesc: TextView? = null
    private var detailTitle: TextView? = null
    private var detailCategory: TextView? = null
    private var detailPrice: TextView? = null
    private var detailImage: ImageView? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var key: String? = ""
    private var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize Views
        detailDesc = findViewById(R.id.detailDescription)
        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailName)
        detailPrice = findViewById(R.id.detailPrice)
        detailCategory = findViewById(R.id.detailCategory)
        deleteButton = findViewById(R.id.deleteButton)
        editButton = findViewById(R.id.editButton)

        // Extract data from bundle
        intent.extras?.let { bundle ->
            detailDesc?.text = bundle.getString("Description")
            detailTitle?.text = bundle.getString("Title")
            detailCategory?.text = bundle.getString("Category")
            detailPrice?.text = bundle.getString("Price")
            key = bundle.getString("Key")
            imageUrl = bundle.getString("Image")
            Glide.with(this).load(imageUrl).into(detailImage ?: return)
        }

//        deleteButton?.setOnClickListener {
//            FirebaseDatabase.getInstance().getReference("Products").let { reference ->
//                // Deletion logic
//                val deleteFromDatabase = {
//                    reference.child(key ?: return@let).removeValue()
//                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//
//                if (!imageUrl.isNullOrEmpty()) {
//                    FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl!!).delete()
//                        .addOnSuccessListener { deleteFromDatabase() }
//                        .addOnFailureListener {
//                            Toast.makeText(
//                                this,
//                                "Image not found in storage, deleting data...",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            deleteFromDatabase()
//                        }
//                } else {
//                    deleteFromDatabase()
//                }
//            }
//        }

        deleteButton?.setOnClickListener {
            // Early exit if key is null
            if (key == null) {
                Toast.makeText(this, "Key is null, cannot delete.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use a local copy of imageUrl to allow smart casting
            val currentImageUrl = imageUrl
            val reference = FirebaseDatabase.getInstance().getReference("Products")

            val deleteFromDatabase = {
                // Proceed with deletion of the product
                reference.child(key!!).removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }

            if (!currentImageUrl.isNullOrEmpty()) {
                FirebaseStorage.getInstance().getReferenceFromUrl(currentImageUrl).delete()
                    .addOnSuccessListener {
                        deleteFromDatabase()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image not found in storage, deleting data...", Toast.LENGTH_SHORT).show()
                        deleteFromDatabase()
                    }
            } else {
                deleteFromDatabase()
            }
        }

        editButton?.setOnClickListener {
            Intent(this, UpdateProduct::class.java).apply {
                putExtra("Title", detailTitle?.text.toString())
                putExtra("Description", detailDesc?.text.toString())
                putExtra("Category", detailCategory?.text.toString())
                putExtra("Price", detailPrice?.text.toString())
                putExtra("Image", imageUrl)
                putExtra("Key", key)
            }.also { startActivity(it) }
        }
    }
}