package com.ray3du.onlinesurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        val drawer = findViewById<ImageView>(R.id.drawer)
        val lecturer = findViewById<CardView>(R.id.lecturer)
        val school = findViewById<CardView>(R.id.school)
        val business = findViewById<CardView>(R.id.business)
        val supermarket = findViewById<CardView>(R.id.supermarket)


        //Handle drawer
        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }

        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_logout -> {
                    mAuth = FirebaseAuth.getInstance()
                    mAuth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("message", "Logged out")
                    startActivity(intent)
                    finish()
                }
                else -> {
                    println("Not pressed")
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        drawer.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        //Handle lecturer
        lecturer.setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("keys", "lecturer")
            startActivity(intent)
            finish()
        }

        //Handle school
        school.setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("keys", "school")
            startActivity(intent)
            finish()
        }

        //Handle business
        business.setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("keys", "business")
            startActivity(intent)
            finish()
        }

        //Handle supermarket
        supermarket.setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("keys", "supermarket")
            startActivity(intent)
            finish()
        }

    }
}