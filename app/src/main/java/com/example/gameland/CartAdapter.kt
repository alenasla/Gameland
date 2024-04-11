package com.example.gameland

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameland.CartAdapter.CartViewHolder

class CartAdapter(private val context: Context, private val cartItems: List<DataClass>) :
    RecyclerView.Adapter<CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        if (item.dataImage != null && !item.dataImage!!.isEmpty()) {
            Glide.with(context).load(item.dataImage).into(holder.cartImage)
        } else {
            holder.cartImage.setImageResource(R.drawable.baseline_image_24)
        }
        holder.cartName.text = item.dataName
        holder.cartCategory.text = item.dataCategory
        holder.cartDescription.text = item.dataDescription
        holder.cartPrice.text = item.dataPrice
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartImage: ImageView
        var cartName: TextView
        var cartCategory: TextView
        var cartDescription: TextView
        var cartPrice: TextView

        init {
            cartImage = itemView.findViewById(R.id.cartImage)
            cartName = itemView.findViewById(R.id.cartName)
            cartCategory = itemView.findViewById(R.id.cartCategory)
            cartDescription = itemView.findViewById(R.id.cartDescription)
            cartPrice = itemView.findViewById(R.id.cartPrice)
        }
    }
}