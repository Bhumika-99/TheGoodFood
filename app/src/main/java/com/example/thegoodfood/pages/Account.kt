package com.example.thegoodfood.pages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thegoodfood.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Account : Fragment() {
    lateinit var email: TextInputEditText
    lateinit var username: TextInputEditText
    lateinit var phonenumber: TextInputEditText
    lateinit var address: TextInputEditText
    lateinit var save: MaterialButton
    lateinit var db: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        email = view.findViewById(R.id.emailaddress)
        username = view.findViewById(R.id.username)
        phonenumber = view.findViewById(R.id.userphone)
        address = view.findViewById(R.id.useraddress)
        save = view.findViewById(R.id.save)
        sharedPreferences = context!!.getSharedPreferences("com.example.thegoodfood", 0)
        db = FirebaseDatabase.getInstance().reference
        db.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (p0.hasChild("email")) {
                            email.setText(p0.child("email").value.toString())
                        }
                        if (p0.hasChild("name")) {
                            username.setText(p0.child("name").value.toString())
                        }
                        if (p0.hasChild("phone")) {
                            phonenumber.setText(p0.child("phone").value.toString())
                            sharedPreferences.edit()
                                .putString("phonenumber", p0.child("phone").value.toString())
                                .apply()
                        }
                        if (p0.hasChild("address")) {
                            address.setText(p0.child("address").value.toString())
                            sharedPreferences.edit()
                                .putString("address", p0.child("address").value.toString())
                                .apply()
                        }
                    }
                }

            })
        save.setOnClickListener {
            savetodb()
        }
        return view
    }

    fun savetodb() {
        val hashMap = hashMapOf<String, String>()
        if (email.text.toString().isNotEmpty()) {
            hashMap["email"] = email.text.toString().trim()
        }
        if (username.text.toString().isNotEmpty()) {
            hashMap["name"] = username.text.toString().trim()
        }
        if (phonenumber.text.toString().isNotEmpty()) {
            hashMap["phone"] = phonenumber.text.toString().trim()
        }
        if (address.text.toString().isNotEmpty()) {
            hashMap["address"] = address.text.toString().trim()
        }
        if (hashMap.isNotEmpty()) {
            db.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .updateChildren(hashMap as Map<String,String>).addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Account Details Updated Successfully!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
