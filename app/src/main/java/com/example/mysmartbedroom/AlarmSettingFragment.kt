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
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.mysmartbedroom.classes.MyAlarm
import com.example.mysmartbedroom.classes.OpenCurtains
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
        val calender: Calendar = Calendar.getInstance()
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val btnSetAlarm = view.findViewById<Button>(R.id.setAlarmBtn)
        val openCurtainsTime = 600000
        btnSetAlarm.setOnClickListener{
            val calender: Calendar = Calendar.getInstance()
            if(Build.VERSION.SDK_INT >= 23){
                calender.set(
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
            }else {
                calender.set(
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH),
                    timePicker.currentHour,
                    timePicker.currentMinute,
                    0
                )
            }
            setOpenCurtainsAlarm(calender.timeInMillis-openCurtainsTime)
            setAlarm(calender.timeInMillis)
        }
        return view
    }

    private fun setOpenCurtainsAlarm(time: Long) {
        val openCurtains = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity,OpenCurtains::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity,0,intent,0)
        openCurtains.set(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun setAlarm(timeInMillis: Long) {
        val wakeup_alarm = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity,MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity,1,intent,0)
        wakeup_alarm.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(activity,"Alarm is Set",Toast.LENGTH_LONG).show()
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