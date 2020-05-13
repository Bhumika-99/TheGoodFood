package com.example.thegoodfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thegoodfood.R
import com.example.thegoodfood.addtocart
import com.example.thegoodfood.fooditemModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fooditemscard.view.*

class Discover_Adapter(var context: Context, var list: ArrayList<fooditemModel>,var sheetview: View,var sheetBehavior: BottomSheetBehavior<RelativeLayout>) :
    RecyclerView.Adapter<Discover_Adapter.FoodHolder>() {
    var iscartview = 0
    var curact=0
    inner class FoodHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById(R.id.recitemname) as TextView
        var price = itemView.findViewById(R.id.recitemprice) as TextView
        var imgitem = itemView.findViewById(R.id.recitemimage) as ImageView
        var showmore = itemView.findViewById(R.id.showmore) as CardView
        fun bind(data: fooditemModel) {

        }
    }

    fun update(fl: ArrayList<fooditemModel>) {
        this.list = fl
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fooditemscard, parent, false)
        return FoodHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        var item = list[position]
        var bmimg = sheetview!!.findViewById(R.id.bmimg) as ImageView
        var name = sheetview!!.findViewById(R.id.foodname) as TextView
        var desc = sheetview!!.findViewById(R.id.fooddesc) as TextView
        var price = sheetview!!.findViewById(R.id.foodprice) as TextView
        var quantity = sheetview!!.findViewById(R.id.foodquantity) as TextView
        var category = sheetview!!.findViewById(R.id.foodcategory) as TextView
        var bmbtn = sheetview!!.findViewById(R.id.bmbtn) as Button
        holder.bind(list[position])
        holder.name.setText(list[position].name)
        holder.price.setText(list[position].price)
        Glide.with(context).load(list[position].imgurl).into(holder.imgitem)
        if(curact==1){
            bmbtn.visibility=View.GONE
        }

        holder.showmore.setOnClickListener {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                Log.d("slidekey",item.name)
                name.text = "Name: ${item.name}"
                desc.text = "Description: ${item.desc}"
                price.text = "Price: ${item.price}"
                quantity.text = "Quantity:${item.cat}"
                category.text = "Category: ${item.type}"
                bmbtn.setOnClickListener {
                    addtocart().addcart(list[position], context)
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
                Glide.with(context).load(item.imgurl).into(bmimg)
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        if (iscartview == 1) {
            holder.itemView.cart2.setImageResource(R.drawable.cancel_logo)
            holder.itemView.cart2.setOnClickListener {
                addtocart().removefromcart(list[position], context)
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemChanged(position, list.size)
                update(list)
            }
        } else {
            holder.itemView.cart2.setOnClickListener {
                addtocart().addcart(list[position], context)
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var imgclick = View.OnClickListener {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}


class FoodModel {
    var cat = ""
    var name = ""
    var desc = ""
    var price = ""
    var likes = ""
    var imageurl = ""
}