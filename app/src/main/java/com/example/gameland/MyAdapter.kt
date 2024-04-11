package com.example.gameland

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyAdapter(private val context: Context, private var dataList: List<DataClass>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        currentItem?.let { item ->
            Glide.with(context).load(item.dataImage).into(holder.recImage)
            holder.recName.text = item.dataName
            holder.recDescription.text = item.dataDescription
            holder.recCategory.text = item.dataCategory
            holder.recPrice.text = item.dataPrice

            holder.recCard.setOnClickListener {
                Intent(context, DetailActivity::class.java).apply {
                    putExtra("Image", item.dataImage)
                    putExtra("Description", item.dataDescription)
                    putExtra("Title", item.dataName)
                    putExtra("Category", item.dataCategory)
                    putExtra("Price", item.dataPrice)
                    putExtra("Key", item.key)
                    context.startActivity(this)
                }
            }

            holder.addToCartButton.setOnClickListener {
                FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                    val cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId)
                    val cartItemId = cartRef.push().key ?: return@setOnClickListener

                    val cartItem = hashMapOf(
                        "dataName" to item.dataName,
                        "dataCategory" to item.dataCategory,
                        "dataDescription" to item.dataDescription,
                        "dataPrice" to item.dataPrice,
                        "dataImage" to item.dataImage
                    )

                    cartRef.child(cartItemId).setValue(cartItem).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
                } ?: Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun searchDataList(searchList: List<DataClass>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recName: TextView = itemView.findViewById(R.id.recName)
    val recCategory: TextView = itemView.findViewById(R.id.recCategory)
    val recDescription: TextView = itemView.findViewById(R.id.recDescription)
    val recPrice: TextView = itemView.findViewById(R.id.recPrice)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
    val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
}