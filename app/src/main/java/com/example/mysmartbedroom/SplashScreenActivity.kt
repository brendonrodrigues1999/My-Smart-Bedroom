package com.example.mysmartbedroom

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.VideoView

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val videoView = findViewById<VideoView>(R.id.videoView)
        val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.splashscreen)
        videoView.setVideoURI(video)
        videoView.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        })
        videoView.start()
    }
}