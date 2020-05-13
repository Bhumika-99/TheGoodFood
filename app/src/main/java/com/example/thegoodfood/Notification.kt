package com.example.thegoodfood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thegoodfood.adapter.NotificationAdapter
import com.example.thegoodfood.adapter.notifmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notification.*

class Notification : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var list: ArrayList<notifmodel>
    lateinit var adapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setSupportActionBar(toolbarnotif)
        list = ArrayList()
        adapter = NotificationAdapter(applicationContext, list)
        notifrec.layoutManager = LinearLayoutManager(applicationContext)
        notifrec.adapter = adapter
        db = FirebaseDatabase.getInstance().reference
        db.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("notification")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() and p0.hasChildren()) {
                        for (ds in p0.children) {
                            val nm = notifmodel()
                            nm.msg = ds.child("msg").value.toString()
                            nm.id = ds.key.toString()
                            list.add(nm)
                        }
                        adapter.update(list)
                    }
                }
            })
    }
}
