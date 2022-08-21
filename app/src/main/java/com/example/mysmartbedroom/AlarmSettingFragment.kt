package com.example.mysmartbedroom

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.mysmartbedroom.classes.MyAlarm
import com.example.mysmartbedroom.classes.OpenCurtains
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmSettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm_setting, container, false)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val doc: DocumentReference = fStore.collection("Users").document(auth.currentUser?.uid.toString())
        val calender: Calendar = Calendar.getInstance()
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val btnSetAlarm = view.findViewById<Button>(R.id.setAlarmBtn)
        val alarmMinute = view.findViewById<TextView>(R.id.alarmMinute)
        val alarmHour = view.findViewById<TextView>(R.id.alarmHour)
        val openCurtainsTime = 600000
        doc.collection("Bedroom Settings").document("NightModeSettings")
            .get().addOnSuccessListener {
                alarmHour.text = it.data?.get("AlarmHour").toString()
                alarmMinute.text = it.data?.get("AlarmMinute").toString()
            }
        btnSetAlarm.setOnClickListener{
            if(Build.VERSION.SDK_INT >= 23){
                alarmHour.text = timePicker.hour.toString()
                alarmMinute.text = timePicker.minute.toString()
            }else{
                alarmHour.text = timePicker.currentHour.toString()
                alarmMinute.text = timePicker.currentMinute.toString()
            }
            doc.collection("Bedroom Settings").document("NightModeSettings")
                .update(hashMapOf("AlarmHour" to alarmHour.text, "AlarmMinute" to alarmMinute.text) as Map<String, Any>)
            Toast.makeText(activity,"Alarm saved",Toast.LENGTH_LONG).show()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmSettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmSettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}