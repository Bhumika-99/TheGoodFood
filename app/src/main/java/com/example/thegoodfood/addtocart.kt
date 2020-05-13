package com.example.thegoodfood

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class addtocart(){
    lateinit var db:DatabaseReference
    fun addcart(fooditemModel: fooditemModel,context:Context){
        db=FirebaseDatabase.getInstance().reference
        db.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("cart")
            .child(fooditemModel.foodid)
            .setValue(fooditemModel).addOnCanceledListener {
                Toast.makeText(context,"Added to cart",Toast.LENGTH_SHORT).show()
            }

    }
    fun removefromcart(fooditemModel: fooditemModel,context: Context){
        db=FirebaseDatabase.getInstance().reference
        db.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("cart")
            .child(fooditemModel.foodid)
            .removeValue().addOnSuccessListener {
                Toast.makeText(context,"Removed from cart",Toast.LENGTH_SHORT).show()
            }

    }
    
}