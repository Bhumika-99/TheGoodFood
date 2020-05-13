package com.example.thegoodfood

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.thegoodfood.adapter.PagerAdapter
import com.example.thegoodfood.adapter.ZoomOutPageTransformer
import com.example.thegoodfood.pages.Account
import com.example.thegoodfood.pages.Cart
import com.example.thegoodfood.pages.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.header.*


class Homepage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var adapter: PagerAdapter
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var db: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout, null,
            R.string.open,
            R.string.close
        ) {}
        sharedPreferences = getSharedPreferences("com.example.thegoodfood", 0)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.drawerArrowDrawable.color =
            ContextCompat.getColor(applicationContext, R.color.white)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        db = FirebaseDatabase.getInstance().reference
        db.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (p0.hasChild("phone")) {
                            sharedPreferences.edit()
                                .putString("phonenumber", p0.child("phone").value.toString())
                                .apply()
                        }
                        if (p0.hasChild("address")) {
                            sharedPreferences.edit()
                                .putString("address", p0.child("address").value.toString())
                                .apply()
                        }
                        if (p0.hasChild("name")) {
                            headname.text = p0.child("name").value.toString()
                        }
                        if (p0.hasChild("email")) {
                            heademail.text = p0.child("email").value.toString()
                        }
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                            val token = it.token
                            db.child("users")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                .child("token").setValue(token.toString())
                        }
                    }
                }

            })
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.support -> {
                    startActivity(Intent(this, Feedback::class.java))
                }
                R.id.about -> {
                    startActivity(Intent(this, about::class.java))
                }
                R.id.logout -> {
                    auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    startActivity(Intent(applicationContext, Login::class.java))
                    finish()
                }

                R.id.cartshow -> {
                    startActivity(Intent(applicationContext, DiscoverActivity::class.java))
                }
                R.id.notification -> {
                    startActivity(Intent(applicationContext, Notification::class.java))
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        adapter =
            PagerAdapter(supportFragmentManager)
        setupPager()
        bottombar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homepage -> {
                    mainpager.currentItem = 0
                }
                R.id.cartpage -> {
                    mainpager.currentItem = 1
                }
                R.id.accountpage -> {
                    mainpager.currentItem = 2
                }
            }
            false

        }
//        mainpager.setOnTouchListener { v, event -> true }

    }

    fun setupPager() {
        val home = Home()
        val cart = Cart()
        val account = Account()
        adapter.addFrag(home)
        adapter.addFrag(cart)
        adapter.addFrag(account)
        mainpager.adapter = adapter
        mainpager.setPageTransformer(
            true,
            ZoomOutPageTransformer()
        )

    }

    fun opendrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun setdrawer() {
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.drawerArrowDrawable.color =
            ContextCompat.getColor(applicationContext, R.color.white)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}