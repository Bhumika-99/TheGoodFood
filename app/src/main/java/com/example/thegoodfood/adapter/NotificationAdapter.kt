package com.example.thegoodfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.thegoodfood.R
import com.example.thegoodfood.addtocart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NotificationAdapter(var context: Context, var list: ArrayList<notifmodel>) :
    RecyclerView.Adapter<NotificationAdapter.notifHolder>() {
    class notifHolder(var itenView: View) : RecyclerView.ViewHolder(itenView) {
        var textmsg = itenView.findViewById<TextView>(R.id.msg)
        var cancel = itenView.findViewById<ImageView>(R.id.msgrem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notifHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notif_row, parent, false)
        return notifHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<notifmodel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: notifHolder, position: Int) {
        holder.textmsg.setText(list[position].msg)
        holder.cancel.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child("notification").child(list[position].id).removeValue().addOnSuccessListener {
                    list.remove(list[position])
                    notifyItemRemoved(position)
                    notifyItemChanged(position,list.size)
                    update(list)
                    Toast.makeText(context, "The msg removed", Toast.LENGTH_SHORT).show()
                }
        }

    }
}

class notifmodel {
    var msg = ""
    var id = ""
}
