package com.example.thegoodfood

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class orderadapter (
    var userList: ArrayList<ordermodal>,
    val context: Context
): RecyclerView.Adapter<orderadapter.ViewHolder>(){
    var isexpnaded=-1
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ordername =itemView.findViewById(R.id.ordername) as TextView
        val oadd =itemView.findViewById(R.id.oadd) as TextView
        val oname=itemView.findViewById(R.id.oname) as TextView
        val qcard=itemView.findViewById<CardView>(R.id.ocard)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v= LayoutInflater.from(context).inflate(R.layout.order_card,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        TransitionManager.beginDelayedTransition(holder.qcard)
        val user=userList[p1]
        holder.ordername.text="Order: ${user.menu_name}"
        holder.oadd.text="Address: ${user.address}"
        holder.oname.text="Name: ${user.name}"
        holder.qcard.setOnClickListener {
            if(holder.ordername.visibility==View.GONE){
                holder.ordername.visibility=View.VISIBLE
            }
            else{
                holder.ordername.visibility=View.GONE

            }
        }
    }
}


