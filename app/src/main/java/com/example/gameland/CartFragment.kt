package com.example.gameland

import android.os.Bundle
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
    private var cartRecyclerView: RecyclerView? = null
    private var cartAdapter: CartAdapter? = null
    private var cartItems: MutableList<DataClass?>? = null
    private var cartRef: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartRecyclerView?.layoutManager = LinearLayoutManager(context)
        cartItems = ArrayList()
        cartAdapter = CartAdapter(requireContext(), (cartItems as ArrayList<DataClass?>).filterNotNull())
        cartRecyclerView?.adapter = cartAdapter

        // We check if the user is logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId)
            loadCartItems()
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun loadCartItems() {
        cartRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cartItems?.clear()
                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(DataClass::class.java)
                    cartItems?.add(item)
                }
                cartAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context,
                    "Error loading cart items: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}