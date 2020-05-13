package com.example.thegoodfood.pages

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.thegoodfood.DiscoverActivity
import com.example.thegoodfood.Homepage
import com.example.thegoodfood.R
import com.example.thegoodfood.adapter.SlidingImage_Adapter
import com.example.thegoodfood.adapter.ZoomOutPageTransformer
import me.relex.circleindicator.CircleIndicator
import java.util.*


class Home : Fragment() {
    var list = arrayOf(
        R.drawable.front1,
        R.drawable.front2,
        R.drawable.front3
    )
    private var currentPage = 0
    private var NUM_PAGES = 0
    lateinit var viewflipper: ViewPager
    lateinit var indicator: CircleIndicator
    lateinit var toolbar: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        toolbar = view.findViewById(R.id.hometoolbar)
        (activity as Homepage).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        (activity as Homepage).supportActionBar!!.title = ""
        (activity as Homepage).supportActionBar!!.setHomeButtonEnabled(true)
        viewflipper = view.findViewById(R.id.viewFlipper)
        indicator = view.findViewById(R.id.indicator)
        (view.findViewById(R.id.casual) as CardView).setOnClickListener {
            discoveroption(it)
        }
        (view.findViewById(R.id.dining) as CardView).setOnClickListener {
            discoveroption(it)
        }
        (view.findViewById(R.id.tea) as CardView).setOnClickListener {
            discoveroption(it)
        }
        (view.findViewById(R.id.juice) as CardView).setOnClickListener {
            discoveroption(it)
        }
        (view.findViewById(R.id.chocolate) as CardView).setOnClickListener {
            discoveroption(it)
        }
        (view.findViewById(R.id.discovermore) as CardView).setOnClickListener {
            discoveroption(it)
        }
        setFlipper()
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            (activity as Homepage)!!.opendrawer()
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu!!.clear()
        val item3 = menu!!.add(0, 1234, Menu.NONE, "Search")
        item3.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        item3.setIcon(R.drawable.ic_search_black_24dp)
        val s = SpannableString(item3.title.toString())
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
        item3.title = s
    }

    fun discoveroption(view: View) {
        var intent = Intent(context, DiscoverActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        when (view.id) {
            R.id.casual -> {
                intent.putExtra("query","Cuisine")
            }
            R.id.dining -> {
                intent.putExtra("query","dining")

            }
            R.id.tea -> {
                intent.putExtra("query","tea")

            }
            R.id.juice -> {
                intent.putExtra("query","juice")

            }
            R.id.chocolate -> {
                intent.putExtra("query","chocolate")

            }
            R.id.discovermore -> {
                intent.putExtra("query","disc")

            }
        }
        context!!.startActivity(intent)
    }

    fun setFlipper() {
        val adapter =
            SlidingImage_Adapter(context!!, list)
        viewflipper.adapter = adapter
        NUM_PAGES = list.size

        // Auto start of viewpager

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewflipper.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 5000, 5000)
        indicator.setViewPager(viewflipper)
        viewflipper.setPageTransformer(
            true,
            ZoomOutPageTransformer()
        )
    }


}
