package com.example.gameland

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gameland.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    interface LogoutCleanup {
        fun onLogoutCleanup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        checkUserStatus()
        setupNavigation()
    }

    private fun checkUserStatus() {
        val user = auth.currentUser
        if (user == null) {
            navigateToLogin()
        } else {
            binding.userDetails.text = user.email
            binding.btnLogout.setOnClickListener {
                logoutUser()
            }
            binding.fab.setOnClickListener {
                val intent = Intent(this, UploadProduct::class.java)
                startActivity(intent)
            }
            // Set HomeFragment as the initial fragment
            replaceFragment(HomeFragment())
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            switchFragment(item.itemId)
            true
        }
    }

    private fun logoutUser() {
        auth.signOut()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun switchFragment(itemId: Int) {
        val fragment: Fragment? = when (itemId) {
            R.id.home -> HomeFragment()
            R.id.searchFragment -> SearchFragment()
            R.id.cartFragment -> CartFragment()
            R.id.profileFragment -> ProfileFragment()
            else -> null
        }
        fragment?.let { replaceFragment(it) }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
