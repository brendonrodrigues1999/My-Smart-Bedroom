package com.example.mysmartbedroom

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TemperatureControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TemperatureControlFragment : Fragment() {
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
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val view = inflater.inflate(R.layout.fragment_temperature_control, container, false)
        val nightTempBtn = view.findViewById<Button>(R.id.nightTempBtn)
        val tempInp = view.findViewById<EditText>(R.id.tempInp)
        val doc: DocumentReference = fStore.collection("Users").document(auth.currentUser?.uid.toString())
        val doc2 = doc.collection("Bedroom Settings").document("NightModeSettings")
        nightTempBtn.setOnClickListener{
            val temp = tempInp.text.toString().toInt()
            if((temp >28) or (temp<16)){
                Toast.makeText(activity,"Can't set to this temperature", Toast.LENGTH_SHORT).show()
            }else{
                doc2.update(hashMapOf("NightTemp" to temp) as Map<String,Any>)
                Toast.makeText(activity,"Temperature saved", Toast.LENGTH_SHORT).show()
            }
        }
        doc2.get().addOnSuccessListener {
            tempInp.setText(it.data?.get("NightTemp").toString())
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
         * @return A new instance of fragment TemperatureControlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TemperatureControlFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}