package com.example.gameland

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var dataList = mutableListOf<DataClass>()
    private lateinit var adapter: MyAdapter
    private val databaseReference by lazy { FirebaseDatabase.getInstance().getReference("Products") }

    private var searchView: SearchView? = null
    private var eventListener: ValueEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        adapter = MyAdapter(requireContext(), dataList) // Assuming context is not null
        recyclerView.adapter = adapter
        searchView = view.findViewById<SearchView>(R.id.search).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { searchList(it) }
                    return true
                }
            })
        }

        loadProductsFromFirebase()
        return view
    }

    private fun loadProductsFromFirebase() {
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(R.layout.progress_layout)
            .create()
        dialog.show()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                dataSnapshot.children.mapNotNullTo(dataList) { it.getValue(DataClass::class.java) }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun searchList(text: String) {
        val searchList = dataList.filter {
            it.dataName?.lowercase()?.contains(text.lowercase()) == true
        }
        adapter.searchDataList(searchList as ArrayList<DataClass>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventListener?.let { databaseReference.removeEventListener(it) }
    }
}