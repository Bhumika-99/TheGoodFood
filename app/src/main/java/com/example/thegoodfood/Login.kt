package com.example.thegoodfood

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth=FirebaseAuth.getInstance()
        databaseRef= FirebaseDatabase.getInstance().reference
        sharedPreferences=getSharedPreferences("com.example.thegoodfood",0)
        checkExistingUser()
        login.setOnClickListener() {
            login()
        }
        subreg.setOnClickListener {
            var i = Intent(this, Signup::class.java)
            startActivity(i)
        }
    }
    fun checkExistingUser(){
        if(firebaseAuth.currentUser!=null) {
            var i = Intent(this,Homepage::class.java)
                startActivity(i)
            finish()

        }
    }
    override fun onStart() {
        super.onStart()

    }
    fun login(){
        val email = email.text.toString().trim()
        val pass = pass.text.toString().trim()
        if(email.isNotEmpty() && pass.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener {
                var i = Intent(this, Homepage::class.java)
                startActivity(i)
            }.addOnFailureListener{
                Toast.makeText(
                    applicationContext,
                    "Please Check Your Credentials & Try Again!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        else{
            Toast.makeText(applicationContext, "Please Fill The Details", Toast.LENGTH_SHORT).show()

        }
    }
}