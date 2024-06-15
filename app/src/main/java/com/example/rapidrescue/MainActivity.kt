@file:Suppress("DEPRECATION")
package com.example.rapidrescue

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Places.initialize(this, "AIzaSyBuviDdojsTPlG6ghdtRR6OwCFFI6qkZCM")

        setUpDrawerLayout()

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
                    true
                } else {
                    drawerLayout.openDrawer(GravityCompat.START) // Open the drawer first
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.slide_in_left, // Enter animation
            android.R.anim.slide_out_right, // Exit animation
        )

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
        binding.navView.visibility = View.GONE

    }


    //For opening options in the drawer layout
    private fun setUpDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.personal_safety -> {
                    val intent = Intent(this, PersonalSafety::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.panic_button -> {
                        val intent = Intent(this, EmergencyContacts::class.java)
                        startActivity(intent)
                }
                R.id.developers_page -> {
                    navigateToFragment(developers()) // Replace with your fragment class
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
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }


}
