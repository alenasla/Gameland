package com.example.gameland

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var cartItems: MutableList<DataClass> = mutableListOf()
    private var cartRef: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(requireContext(), cartItems)
        cartRecyclerView.adapter = cartAdapter

        val user = FirebaseAuth.getInstance().currentUser
        user?.uid?.let {
            cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(it)
            loadCartItems()
        } ?: Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()

        return view
    }

    private fun loadCartItems() {
        cartRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cartItems.clear()
                dataSnapshot.children.mapNotNullTo(cartItems) { it.getValue(DataClass::class.java) }
                cartAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Error loading cart items: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}