package com.example.mysmartbedroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            startActivity(Intent(applicationContext, RegisterUserActivity::class.java))
            finish()
        }
        val loginBtn = findViewById<Button>(R.id.LoginBtn)
        loginBtn.setOnClickListener{
            val email = findViewById<EditText>(R.id.emailInput).text.toString()
            val password = findViewById<EditText>(R.id.passwordInput).text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Empty Fields", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        Toast.makeText(this,"Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
            }
        }

    }
}