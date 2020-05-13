package com.example.thegoodfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thegoodfood.adapter.queryadapter
import com.google.firebase.database.*

class AdminFeedback : AppCompatActivity() {

    lateinit var db: DatabaseReference
    lateinit var adapter: queryadapter
    lateinit var qlist:ArrayList<query>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_feedback)
        db= FirebaseDatabase.getInstance().reference
        val recyclerView= findViewById<RecyclerView>(R.id.queryrec)
        recyclerView.layoutManager= LinearLayoutManager(this)
        qlist=ArrayList<query>()
        val adapter= queryadapter(
            qlist,
            applicationContext
        )
        recyclerView.adapter=adapter
        db.child("Admin").child("Queries").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("e","error")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    if(p0.hasChildren()){
                        for (ds in p0.children){
                            var t=query(
                                ds.child("qtext").value.toString(),
                                ds.child("email").value.toString(),
                                ds.child("name").value.toString()
                            )
                            qlist.add(t)
                        }
                        adapter.userList=qlist
                        adapter.notifyDataSetChanged()

                    }
                }
            }

        })}
}

