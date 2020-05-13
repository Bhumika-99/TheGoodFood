package com.example.thegoodfood.pages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thegoodfood.Order
import com.example.thegoodfood.R
import com.example.thegoodfood.adapter.Discover_Adapter
import com.example.thegoodfood.fooditemModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cart.*

class Cart : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var paybtn: Button
    lateinit var db: DatabaseReference
    lateinit var adapter: Discover_Adapter
    lateinit var list: ArrayList<fooditemModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarcart)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        recyclerView = view.findViewById(R.id.cartrecy)
        paybtn = view.findViewById(R.id.pay)
        list = ArrayList()
        db = FirebaseDatabase.getInstance().reference
        var bottom_sheet = view.findViewById<RelativeLayout>(R.id.bottom_sheet)
        var sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                var bmimg = bottomSheet.findViewById(R.id.bmimg) as ImageView
                bmimg.minimumHeight = (200 * slideOffset * resources.displayMetrics.density).toInt()
                bmimg.minimumWidth = (200 * slideOffset * resources.displayMetrics.density).toInt()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
//
        adapter = Discover_Adapter(context!!, list,bottom_sheet,sheetBehavior)
        adapter.curact=1
//        adapter = Discover_Adapter(context!!, list)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        adapter.iscartview = 1
        paybtn.setOnClickListener {
            val intent = Intent(context, Order::class.java)
            intent.putExtra("list", list)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
        }
        getdata()
        setHasOptionsMenu(true)
        return view
    }

    fun getdata() {

        db.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
            .child("cart").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    if (p0.exists() and p0.hasChildren()) {
                        for (ds in p0.children) {
                            var fooditemModel = fooditemModel(
                                ds.child("imgurl").value.toString(),
                                ds.child("name").value.toString(),
                                ds.child("price").value.toString(),
                                ds.child("desc").value.toString(),
                                ds.child("type").value.toString(),
                                ds.child("cat").value.toString()
                            )
                            fooditemModel.setid(ds.key.toString())
                            list.add(fooditemModel)
                        }
                        adapter.update(list)
                        if (list.isEmpty()) {
                            msg.visibility = View.VISIBLE
                        } else {
                            msg.visibility = View.GONE
                        }
                    }
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getdata()
    }
}
