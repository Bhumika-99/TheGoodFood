package com.example.thegoodfood

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_order.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Order : AppCompatActivity() {
    lateinit var list: ArrayList<fooditemModel>
    lateinit var myCalendar: Calendar
    lateinit var db: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbarorder)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        toolbarorder.navigationIcon = resources.getDrawable(
            R.drawable.ic_close_black_24dp,
            null
        )
        toolbarorder.setNavigationOnClickListener {
            finish()
        }
        sharedPreferences = getSharedPreferences("com.example.thegoodfood", 0)

        if (sharedPreferences.contains("address")) {
            address.setText(sharedPreferences.getString("address", ""))
        }
        if (!sharedPreferences.contains("phonenumber")) {
            var a = AlertDialog.Builder(this)
                .setTitle("Information")
                .setMessage("Please add your mobile no in Account section")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
                .create()
                .show()
        }
        list = intent.extras!!.get("list") as ArrayList<fooditemModel>
        db = FirebaseDatabase.getInstance().reference
        myCalendar = Calendar.getInstance();
        val date = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR,hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            updateLabel()
        }
        var price = 0
        var total = ""
        for (ds in list) {
            price += ds.price.toInt()
            total = price.toString()
        }
        totalprice.setText("$total")
        itmes.setText(list.size.toString())
        deldate.setOnClickListener {
            TimePickerDialog(
                this, date, myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE],false).show()
        }
        people.setText("1")
        people.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "The field cant be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    totalprice.setText(price.toString())

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    if (s.toString().toInt() != 0) {
                        val price2 = price * s.toString().toInt()
                        total = price2.toString()
                        totalprice.setText(total)
                    }
                    if (s.toString().toInt() == 0) {
                        Toast.makeText(applicationContext, "zero isn't valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        })
        ordernow.setOnClickListener {
            if (address.text.toString().isNotEmpty() && deldate.text.toString()
                    .isNotEmpty() && people.text.toString().isNotEmpty()
            ) {
                progorder.visibility = View.VISIBLE
                val ref = db.child("Admin").child("Orders")
                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()!!)
                    .child(UUID.randomUUID().toString())
                var foodlist = ArrayList<String>()
                var hashMap = hashMapOf<String, Any>()
                list.forEach {
                    foodlist.add(it.foodid)
                }
                hashMap["foodlist"] = foodlist
                hashMap["price"] = totalprice.text.toString().trim()
                hashMap["items"] = list.size
                hashMap["add"] = address.text.toString().trim()
                hashMap["del"] = deldate.text.toString().trim()
                hashMap["ordereddate"] = Calendar.getInstance().time.toString().trim()
                hashMap["people"] = people.text.toString().trim()
                hashMap["status"]="0"
                ref.setValue(hashMap).addOnSuccessListener {
                    progorder.visibility = View.GONE
                    list.forEach {
                        addtocart().removefromcart(it, applicationContext)
                    }
                    Toast.makeText(
                        applicationContext,
                        "Thanks for ordering from our app!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }


    private fun updateLabel() {
        val myFormat = "hh:mm a" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        deldate.setText(sdf.format(myCalendar.getTime()))
    }


}

