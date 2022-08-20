package com.example.mysmartbedroom.classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OpenCurtains : BroadcastReceiver() {
    private lateinit var auth: FirebaseAuth
    override fun onReceive(p0: Context?, p1: Intent?) {
        auth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference(auth.currentUser?.uid.toString())
        ref.child("Curtains").setValue("open")
        Toast.makeText(p0,"Alarm is Set",Toast.LENGTH_LONG).show()
    }
}