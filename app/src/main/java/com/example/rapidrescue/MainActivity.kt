@file:Suppress("DEPRECATION")
package com.example.rapidrescue

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rapidrescue.databinding.ActivityMainBinding
import com.example.rapidrescue.ui.BugReport.bug_report
import com.example.rapidrescue.ui.EmergencyContacts.EmergencyContacts
import com.example.rapidrescue.ui.PersonalSafety.PersonalSafety
import com.example.rapidrescue.ui.WeatherSafety.WeatherSafety
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Places.initialize(this, "google_maps_key")

        auth = FirebaseAuth.getInstance()
        drawerLayout = binding.drawerLayout

        setupDrawerLayout() // Setup drawer layout

        // Setup bottom navigation
        val bottomNavView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        bottomNavView.setupWithNavController(navController)

        // Configure action bar with navigation controller and drawer layout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_chatbot
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up ActionBarDrawerToggle
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.app_name,
            R.string.app_name
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable the back button

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            navigateToDestination(menuItem)
            true
        }
    }

    // For bottom navigation
    private fun navigateToDestination(menuItem: MenuItem) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        when (menuItem.itemId) {
            R.id.navigation_home -> navController.navigate(R.id.navigation_home)
            R.id.navigation_dashboard -> navController.navigate(R.id.navigation_dashboard)
            R.id.navigation_notifications -> navController.navigate(R.id.navigation_notifications)
            R.id.navigation_chatbot -> navController.navigate(R.id.navigation_chatbot)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        if (navController.currentDestination?.id == R.id.navigation_home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        } else {
            navController.navigateUp()
        }
    }

    // Setup Drawer Layout
    private fun setupDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.app_name,
            R.string.app_name
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Handle Drawer Navigation
    private fun handleDrawerNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.personal_safety -> {
                val intent = Intent(this, PersonalSafety::class.java)
                startActivity(intent)
            }
            R.id.panic_button -> {
                val intent = Intent(this, EmergencyContacts::class.java)
                startActivity(intent)
            }
            R.id.developers_page -> {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.developers_btn)
            }
            R.id.weather_safety -> {
                val intent = Intent(this, WeatherSafety::class.java)
                startActivity(intent)
            }
            R.id.bug_report -> {
                val intent = Intent(this, bug_report::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
