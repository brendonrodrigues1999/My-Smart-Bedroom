package com.example.mysmartbedroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        val drawerlayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val menuBtn = findViewById<ImageView>(R.id.imageMenu)
        menuBtn.setOnClickListener{
            drawerlayout.openDrawer(GravityCompat.START)
        }
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.menuProfile -> {

                    true
                }
                R.id.menuLogout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this,"Logged Out..", Toast.LENGTH_SHORT).show()
                    true
                }
                else-> false
            }
        }
    }
}