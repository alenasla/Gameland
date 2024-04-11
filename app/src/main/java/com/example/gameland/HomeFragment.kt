package com.example.gameland

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: MutableList<DataClass>
    private lateinit var adapter: MyAdapter
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        dataList = ArrayList()
        adapter = MyAdapter(requireContext(), dataList) // Note: Ensure MyAdapter can handle a non-nullable List<DataClass>
        recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Products")
        loadProductsFromFirebase()
        return view
    }

    private fun loadProductsFromFirebase() {
        eventListener = databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in dataSnapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.key = itemSnapshot.key // Assuming DataClass has a 'key' property
                    dataClass?.let { dataList.add(it) }
                }

//                for (itemSnapshot in dataSnapshot.children) {
//                    val dataClass = itemSnapshot.getValue(DataClass::class.java)?.apply {
//                        key = itemSnapshot.key // Directly setting the key here
//                    }
//                    if (dataClass != null) {
//                        dataList.add(dataClass)
//                    }
//                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        eventListener?.let { databaseReference?.removeEventListener(it) }
    }
}