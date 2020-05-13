package com.example.thegoodfood.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.thegoodfood.R


class SlidingImage_Adapter(private val context: Context, private val IMAGES: Array<Int>) :
    PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = inflater.inflate(R.layout.homeimageslider, view, false)!!
        val imageView: ImageView = imageLayout
            .findViewById(R.id.sliderimage) as ImageView
        imageView.setImageResource(IMAGES[position])
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun restoreState(
        state: Parcelable?,
        loader: ClassLoader?
    ) {
    }

    override fun saveState(): Parcelable? {
        return null
    }


}