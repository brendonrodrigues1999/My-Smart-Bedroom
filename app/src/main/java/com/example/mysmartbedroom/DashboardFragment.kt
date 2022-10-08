package com.example.mysmartbedroom

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.example.mysmartbedroom.classes.Bedroom
import com.example.mysmartbedroom.classes.GridViewModal
import com.example.mysmartbedroom.classes.MyAlarm
import com.example.mysmartbedroom.classes.OpenCurtains
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    lateinit var iotGRV: GridView
    lateinit var iotList: List<GridViewModal>

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
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val tempView = view.findViewById<TextView>(R.id.temperatureView)
        val nightModeBtn = view.findViewById<Button>(R.id.nightModeBtn)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference(auth.currentUser?.uid.toString())
        val doc: DocumentReference = fStore.collection("Users").document(auth.currentUser?.uid.toString())
        var bedroom = Bedroom("","","",0.0,"","","")
        iotGRV = view.findViewById(R.id.gridView)
        iotList = ArrayList<GridViewModal>()
        iotList = iotList + GridViewModal("Lights",R.drawable.light_off)
        iotList = iotList + GridViewModal("Curtains",R.drawable.closed_curtains)
        iotList = iotList + GridViewModal("Music",R.drawable.music_on)
        iotList = iotList + GridViewModal("Air Conditioner",R.drawable.ac_off)
        iotList = iotList + GridViewModal("Alarm",R.drawable.alarm)
        iotList = iotList + GridViewModal("Door Locks",R.drawable.door_unlocked)

        val iotAdapter = context?.let { GridAdapter(iotList = iotList, it.applicationContext) }
        iotGRV.adapter = iotAdapter
        iotGRV.onItemClickListener = AdapterView.OnItemClickListener{_,_,position,_ ->
            val tempFrag = TemperatureControlFragment.newInstance("param","param")
            val alarmFreg = AlarmSettingFragment.newInstance("param1","param2")
            when (iotList[position].iotName){
                "Lights" ->
                    if(bedroom.Lights == "on"){
                        ref.child("Lights").setValue("off")
                    }else{
                        ref.child("Lights").setValue("on")
                    }
                "Curtains" ->
                    if(bedroom.Curtains == "open"){
                        ref.child("Curtains").setValue("close")
                    }else{
                        ref.child("Curtains").setValue("open")
                    }
                "Music" ->
                    if(bedroom.Music == "on"){
                        ref.child("Music").setValue("off")
                    }else{
                        ref.child("Music").setValue("on")
                    }
                "Air Conditioner" -> {
                    if(bedroom.AC == "off"){
                        val doc2 = doc.collection("Bedroom Settings").document("AirConditionerSettings")
                        doc2.get().addOnSuccessListener {
                            val temp = it.data?.get("Temperature").toString()
                            ref.child("AC").setValue(temp)
                        }
                    }else{
                        ref.child("AC").setValue("off")
                    }
                }
                "Alarm" -> {
                    view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.add(R.id.menuHome, alarmFreg)?.disallowAddToBackStack()?.commit()
                }
                "Door Locks" ->
                    if(bedroom.Door_Locks == "locked"){
                        ref.child("Door_Locks").setValue("unlocked")
                    }else{
                        ref.child("Door_Locks").setValue("locked")
                    }
            }
        }

        iotGRV.onItemLongClickListener =
            AdapterView.OnItemLongClickListener {_,_,position,_ ->
                val lightsFreg = LightsControlFragment.newInstance("param1","param2")
                val curtainsFreg = CurtainsControlFragment.newInstance("param1","param2")
                val musicFreg = MusicControlFragment.newInstance("param1","param2")
                val tempFrag = TemperatureControlFragment.newInstance("param1","param2")
                val alarmFreg = AlarmSettingFragment.newInstance("param1","param2")
                val doorFreg = DoorLocksControlFragment.newInstance("param1","param2")
                when (iotList[position].iotName) {
                    "Lights" -> {
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, lightsFreg)?.disallowAddToBackStack()?.commit()
                    }
                    "Curtains" -> {
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, curtainsFreg)?.disallowAddToBackStack()?.commit()
                    }
                    "Music" -> {
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, musicFreg)?.disallowAddToBackStack()?.commit()
                    }
                    "Air Conditioner" -> {
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, tempFrag)?.disallowAddToBackStack()?.commit()
                    }
                    "Alarm" -> {
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, alarmFreg)?.disallowAddToBackStack()?.commit()
                    }
                    "Door Locks" ->{
                        view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.menuHome, doorFreg)?.disallowAddToBackStack()?.commit()
                    }
                }
                true
            }


        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                Log.w("TAG", dataSnapshot.toString())
                bedroom = dataSnapshot.getValue(Bedroom::class.java)!!
                tempView.text = bedroom.Temperature.toString()
                if(bedroom.Lights=="on"){
                    iotGRV.get(0).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.lights_on)
                }else if(bedroom.Lights=="off"){
                    iotGRV.get(0).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.light_off)
                }
                if(bedroom.Curtains=="open"){
                    iotGRV.get(1).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.open_curtains)
                }else if(bedroom.Curtains=="close"){
                    iotGRV.get(1).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.closed_curtains)
                }
                if(bedroom.Music=="on"){
                    iotGRV.get(2).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.music_on)
                }else if(bedroom.Music=="off"){
                    iotGRV.get(2).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.music_off)
                }
                if(bedroom.Door_Locks=="locked"){
                    iotGRV.get(5).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.door_locked)
                }else if(bedroom.Door_Locks=="unlocked"){
                    iotGRV.get(5).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.door_unlocked)
                }
                if(bedroom.AC == "off"){
                    iotGRV.get(3).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.ac_off)
                }else{
                    iotGRV.get(3).findViewById<ImageView>(R.id.iotIcon).setImageResource(R.drawable.ac_on)
                }
                if(bedroom.Night_Mode =="on"){
                    nightModeBtn.text = "TURN OFF NIGHT MODE"
                }else if(bedroom.Night_Mode =="off"){
                    nightModeBtn.text = "TURN ON NIGHT MODE"
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }

        })

        nightModeBtn.setOnClickListener{
            if(bedroom.Night_Mode =="on"){
                ref.child("Night_Mode").setValue("off")
            }else if(bedroom.Night_Mode =="off"){
                setNightMode(ref,doc)
            }
        }

        nightModeBtn.setOnLongClickListener{
            val nightmodeFrag = NightModeSettingsFragment.newInstance("param1","param2")
            view.findViewById<ConstraintLayout>(R.id.menuHome).removeAllViews()
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.menuHome, nightmodeFrag)?.disallowAddToBackStack()?.commit()
            true
        }

        return view
    }

    private fun setNightMode(ref: DatabaseReference, doc: DocumentReference) {
        val openCurtainsTime = 12000000
        ref.child("Night_Mode").setValue("on")
        doc.collection("Bedroom Settings").document("NightModeSettings")
            .get().addOnSuccessListener {
            if(it.data?.get("Lights") =="enabled"){
                ref.child("Lights").setValue("off")
            }
            if(it.data?.get("Curtains")=="enabled"){
                ref.child("Curtains").setValue("close")
            }
            if(it.data?.get("Air Conditioner")=="enabled"){
                val userNightTemp = it.data?.get("NightTemp").toString()
                ref.child("AC").setValue(userNightTemp)
            }
            if(it.data?.get("Door_Locks")=="enabled"){
                ref.child("Door_Locks").setValue("locked")
            }
            if(it.data?.get("Music")=="enabled"){
                ref.child("Music").setValue("on")
            }
            setAlarm(getTime(it)) //set alarm based on time set by user
            setOpenCurtainsAlarm(getTime(it)-openCurtainsTime) //set curtain to open 20 mins before wakeup alarm
        }
    }

    private fun getTime(it: DocumentSnapshot?): Long {
            val calender: Calendar = Calendar.getInstance()
        calender.set(
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH),
            it?.data?.get("AlarmHour").toString().toInt(),
            it?.data?.get("AlarmMinute").toString().toInt(),
            0
        )
        return calender.timeInMillis
    }

    fun setAlarm(timeInMillis: Long) {
        val wakeup_alarm = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity,1,intent,0)
        wakeup_alarm.set(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    private fun setOpenCurtainsAlarm(time: Long) {
        val openCurtains = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, OpenCurtains::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity,0,intent,0)
        openCurtains.set(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}