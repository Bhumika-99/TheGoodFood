package com.example.thegoodfood

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var databaseRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseRef= FirebaseDatabase.getInstance().reference
        signup_btn.setOnClickListener {
            sign()

        }
        sublogin.setOnClickListener {
            startActivity(Intent(applicationContext, Login::class.java))
        }
    }
    fun sign(){
        val email=signup_email.text.toString().trim()
        val pass=signup_password.text.toString().trim()
        if(email.isNotEmpty() &&
            pass.isNotEmpty() &&
            signup_name.text.toString().trim().isNotEmpty()) {
                var name = signup_name.text.toString().trim()
                var hasmap = hashMapOf<String, String>()
                hasmap["email"] = email
                hasmap["name"] = name
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {

                    databaseRef.child("users").child(it.user?.uid!!.toString()).setValue(hasmap)
                        .addOnSuccessListener {
                            startActivity(Intent(applicationContext, Homepage::class.java))
                            finish()
                        }
                }.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Please check your credentials and Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        else{
            Toast.makeText(applicationContext, "Please Fill The Details", Toast.LENGTH_SHORT).show()

        }
    }

}
