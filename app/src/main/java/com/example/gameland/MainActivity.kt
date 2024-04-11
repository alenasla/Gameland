package com.example.gameland

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.gameland.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var binding: ActivityMainBinding? = null
    var recyclerView: RecyclerView? = null
    var dataList: List<DataClass>? = null
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
        val user = auth!!.currentUser


        // checking if the user is logged in
        if (user == null) {
            // In case user is not logged in, we navigate him to the Login screen
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
            return
        } else {
            // If user is logged, the application opens with main page showing up
            binding!!.userDetails.text = user.email
            binding!!.btnLogout.setOnClickListener { view: View? ->
                auth!!.signOut()
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
            }
            binding!!.fab.setOnClickListener { view: View? ->
                val intent = Intent(this@MainActivity, UploadProduct::class.java)
                startActivity(intent)
            }

            // Adding a primary fragment
            replaceFragment(HomeFragment())

            // When clicked on the available options in the bottom navigation bar, the correct screen fragment will open
            binding!!.bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
                switchFragment(item.itemId)
                true
            }
        }
    }

    private fun switchFragment(itemId: Int) {
        var fragment: Fragment? = null
        if (itemId == R.id.home) {
            fragment = HomeFragment()
        } else if (itemId == R.id.searchFragment) {
            fragment = SearchFragment()
        } else if (itemId == R.id.cartFragment) {
            fragment = CartFragment()
        } else if (itemId == R.id.profileFragment) {
            fragment = ProfileFragment()
        }
        fragment?.let { replaceFragment(it) }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}