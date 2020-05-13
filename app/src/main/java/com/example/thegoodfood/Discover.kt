package com.example.thegoodfood

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thegoodfood.adapter.Discover_Adapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_discover.*


class DiscoverActivity : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var adapter: Discover_Adapter
    var searchquery = ""

    lateinit var list: ArrayList<fooditemModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        list = ArrayList()
        db = FirebaseDatabase.getInstance().reference
        var bottom_sheet = findViewById<RelativeLayout>(R.id.bottom_sheet)
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
        adapter = Discover_Adapter(applicationContext, list,bottom_sheet,sheetBehavior)
//        adapter.setsheet(sheetBehavior, bottom_sheet)
        cartrec.layoutManager = GridLayoutManager(applicationContext, 2)
        cartrec.adapter = adapter
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchinDb(newText!!, "no")
                return true
            }

        })
        if (intent.hasExtra("query")) {
            searchquery = intent.extras?.getString("query")!!
            if (searchquery == "disc") {
                searchinDb("", searchquery)
            } else {
                searchbar.setQuery(searchquery, true)
                searchinDb("", searchquery)
            }

        }
    }

    fun searchinDb(s: String, type: String) {
        list.clear()
        if (type == "no") {
            db.child("Admin").child("XAk5RJc3YROwvdEvFZ3RDfCfXoD2")
                .orderByChild("name")
                .startAt(s.capitalize())
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                for (ds in p0.children) {
                                    if (ds.child("name").value.toString().contains(s)) {
                                        var fooditemModel = fooditemModel(
                                            ds.child("url").value.toString(),
                                            ds.child("name").value.toString(),
                                            ds.child("price").value.toString(),
                                            ds.child("desc").value.toString(),
                                            ds.child("type").value.toString(),
                                            ds.child("cat").value.toString()

                                        )
                                        fooditemModel.setid(ds.key.toString())
                                        list.add(fooditemModel)
                                        adapter.update(list)
                                    }
                                }
                            }
                        }
                    })
        } else {
            var ref = db.child("Admin").child("XAk5RJc3YROwvdEvFZ3RDfCfXoD2")
            if (type != "disc") {
                ref.orderByChild("type")
                    .startAt(type).addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    for (ds in p0.children) {
                                        if (ds.child("type").value.toString().contains(type)) {

                                            var fooditemModel = fooditemModel(
                                                ds.child("url").value.toString(),
                                                ds.child("name").value.toString(),
                                                ds.child("price").value.toString(),
                                                ds.child("desc").value.toString(),
                                                ds.child("type").value.toString(),
                                                ds.child("cat").value.toString()
                                            )
                                            fooditemModel.setid(ds.key.toString())
                                            list.add(fooditemModel)
                                            adapter.update(list)
                                        }
                                    }
                                }
                            }
                        })
            } else {
                ref.limitToFirst(50).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                for (ds in p0.children) {
                                    var fooditemModel = fooditemModel(
                                        ds.child("url").value.toString(),
                                        ds.child("name").value.toString(),
                                        ds.child("price").value.toString(),
                                        ds.child("desc").value.toString(),
                                        ds.child("type").value.toString(),
                                        ds.child("cat").value.toString()
                                    )
                                    fooditemModel.setid(ds.key.toString())
                                    list.add(fooditemModel)
                                    adapter.update(list)

                                }
                            }
                        }
                    })
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

