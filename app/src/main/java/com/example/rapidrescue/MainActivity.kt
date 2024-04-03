@file:Suppress("DEPRECATION")

package com.example.rapidrescue

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rapidrescue.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private var backPressedTime = 0L
    private val delay = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout

        // Setup bottom navigation
        val bottomNavView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavView.setupWithNavController(navController)

        // Configure action bar with navigation controller and drawer layout
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_chatbot
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            navigateToDestination(menuItem)
            true
        }
    }
    //For bottom navigation
    private fun navigateToDestination(menuItem: MenuItem) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        when (menuItem.itemId) {
            R.id.navigation_home -> navController.navigate(R.id.navigation_home)
            R.id.navigation_dashboard -> navController.navigate(R.id.navigation_dashboard)
            R.id.navigation_notifications -> navController.navigate(R.id.navigation_notifications)
            R.id.navigation_chatbot -> navController.navigate(R.id.navigation_chatbot)
        }
    }
    //For opening and closing drawer layout
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //For closing the application
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (backPressedTime + delay > System.currentTimeMillis()) {
                super.onBackPressed() // Call super only when not handling navigation drawer
            } else {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

}