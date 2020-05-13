package com.example.thegoodfood

import java.io.Serializable

class fooditemModel(var imgurl:String,var name:String,var price:String,var desc:String,var type:String,var cat:String):Serializable {
    var foodid:String=""
    fun setid(id:String){
        this.foodid=id
    }
}