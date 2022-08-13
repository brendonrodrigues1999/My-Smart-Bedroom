package com.example.mysmartbedroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            startActivity(Intent(applicationContext, RegisterUserActivity::class.java))
            finish()
        }

    }
}