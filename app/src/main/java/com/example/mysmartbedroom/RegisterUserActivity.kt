package com.example.mysmartbedroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        fStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        var userId =""
        val registerBtn = findViewById<Button>(R.id.registerBtn1)
        registerBtn.setOnClickListener{
            val f_name = findViewById<EditText>(R.id.f_nameInput).text.toString()
            val s_name = findViewById<EditText>(R.id.s_nameInput).text.toString()
            val mobno = findViewById<EditText>(R.id.mobnoInput).text.toString().toInt()
            val email = findViewById<EditText>(R.id.emailInput1).text.toString()
            val password = findViewById<EditText>(R.id.passInput).text.toString()
            if(f_name.isEmpty() || s_name.isEmpty() || mobno.equals(null) || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Empty Fields", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            userId = auth.currentUser?.uid.toString()
                            val doc: DocumentReference = fStore.collection("Users").document(userId)
                            val user = hashMapOf("FirstName" to f_name,"LastName" to s_name,"Email" to email, "MobileNo" to mobno, "Password" to password)
                            doc.set(user as Map<String, Any>).addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val ref = FirebaseDatabase.getInstance().getReference(userId)
                                    ref.child("Main Lights").setValue("off")
                                    ref.child("Curtains").setValue("close")
                                    ref.child("Temperature").setValue(0)
                                    Toast.makeText(this,"User Added!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                    finish()
                                } else {
                                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(this,"Couldn't Add Info", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(baseContext, "User exists.."+it.exception,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}