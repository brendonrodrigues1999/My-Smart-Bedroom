package com.example.mysmartbedroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        var userID = ""
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().uid.toString()
        val doc: DocumentReference = fStore.collection("Users").document(userID)
        doc.get().addOnSuccessListener {
            var userName = it.data?.get("FirstName")
            val hView = navigationView.getHeaderView(0)
            val nav_user = hView.findViewById<TextView>(R.id.user_name)
            nav_user.setText("Hello, "+userName.toString())
        }

        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        val drawerlayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val menuBtn = findViewById<ImageView>(R.id.imageMenu)
        menuBtn.setOnClickListener{
            drawerlayout.openDrawer(GravityCompat.START)
        }
        val navContoller = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationView,navContoller)
    }
}