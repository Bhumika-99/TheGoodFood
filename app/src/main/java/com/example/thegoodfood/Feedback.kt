package com.example.thegoodfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_feedback.*

class Feedback : AppCompatActivity() {

    lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        setSupportActionBar(feedbacktoolbar)
        db = FirebaseDatabase.getInstance().reference
        qsubmit.setOnClickListener {
            validateMenuItem()
        }
    }

    private fun validateMenuItem() {
        var qtext = qtext.text.toString().trim()
        if (qtext.isNotEmpty()) {
            var a = AlertDialog.Builder(this)
                .setTitle("Add Query")
                .setMessage(" Are you sure you want to add this")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    uploadMenuItem(qtext)
                    //  Toast.makeText(this," Query added",Toast.LENGTH_SHORT)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .create()
                .show()

        } else {
            Toast.makeText(applicationContext, "Please Enter Valid Info ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMenuItem(qtext: String) {
        db.child("User").child(getInstance().currentUser!!.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var hashMap = hashMapOf(
                        "qtext" to qtext,
                        "email" to getInstance().currentUser!!.email.toString(),
                        "name" to p0.child("username").value.toString()
                    )
                    db.child("Admin")
                        .child("Queries")
                        .child(getInstance().currentUser!!.uid.toString())
                        .setValue(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                "Query is added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            })
    }
}
