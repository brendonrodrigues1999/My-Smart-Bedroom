package com.example.mysmartbedroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NightModeSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NightModeSettingsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
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
        val view =  inflater.inflate(R.layout.fragment_night_mode_settings, container, false)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val lightsToggle = view.findViewById<ToggleButton>(R.id.lightsToggle)
        val curtainsToggle = view.findViewById<ToggleButton>(R.id.curtainsToggle)
        val musicToggle = view.findViewById<ToggleButton>(R.id.musicToggle)
        val tempToggle = view.findViewById<ToggleButton>(R.id.tempToggle)
        val locksToggle = view.findViewById<ToggleButton>(R.id.locksToggle)
        val doc: DocumentReference = fStore.collection("Users").document(auth.currentUser?.uid.toString())
        val doc2 = doc.collection("Bedroom Settings").document("NightModeSettings")
        doc2.get().addOnSuccessListener {
            if(it.data?.get("Lights") == "enabled"){
                lightsToggle.toggle()
            }
            if(it.data?.get("Temperature") == "enabled"){
                tempToggle.toggle()
            }
            if(it.data?.get("Curtains") == "enabled"){
                curtainsToggle.toggle()
            }
            if(it.data?.get("Music") == "enabled"){
                musicToggle.toggle()
            }
            if(it.data?.get("Door_Locks") == "enabled"){
                locksToggle.toggle()
            }
        }

        lightsToggle.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked) doc2.update(hashMapOf("Lights" to "enabled")as Map<String, Any>)
            else doc2.update(hashMapOf("Lights" to "disabled")as Map<String, Any>)
        }
        curtainsToggle.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked) doc2.update(hashMapOf("Curtains" to "enabled")as Map<String, Any>)
            else doc2.update(hashMapOf("Curtains" to "disabled")as Map<String, Any>)
        }
        musicToggle.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked) doc2.update(hashMapOf("Music" to "enabled")as Map<String, Any>)
            else doc2.update(hashMapOf("Music" to "disabled")as Map<String, Any>)
        }
        tempToggle.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked) doc2.update(hashMapOf("Temperature" to "enabled")as Map<String, Any>)
            else doc2.update(hashMapOf("Temperature" to "disabled")as Map<String, Any>)
        }
        locksToggle.setOnCheckedChangeListener{_,isChecked ->
            if(isChecked) doc2.update(hashMapOf("Door_Locks" to "enabled")as Map<String, Any>)
            else doc2.update(hashMapOf("Door_Locks" to "disabled")as Map<String, Any>)
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
         * @return A new instance of fragment NightModeSettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NightModeSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}