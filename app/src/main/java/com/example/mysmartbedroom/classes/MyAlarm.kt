package com.example.mysmartbedroom.classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import com.example.mysmartbedroom.WakeUpActivity

class MyAlarm : BroadcastReceiver() {

    override fun onReceive(context : Context, intent : Intent) {
        val intent = Intent(context,WakeUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        MediaPlayerService.play(context)
    }
    object MediaPlayerService {
        var mediaPlayer : MediaPlayer = MediaPlayer()
        fun play(context:Context){

            mediaPlayer = MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
        fun stop(){
            mediaPlayer.stop()

        }
    }
}