package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val actionbar = supportActionBar
        val imageView: ImageView = findViewById(R.id.poster_image)
        val rating_tv = findViewById<TextView>(R.id.mRating)
        val title_tv = findViewById<TextView>(R.id.mTitle)
        val overview_tv = findViewById<TextView>(R.id.movervie_tv)
        val bundle = intent.extras
        val mTitle = bundle!!.getString("title")
        val mPoster = bundle.getString("poster")
        val mOverView = bundle.getString("overview")
        val mRating = bundle.getDouble("rating")
        toolbar.subtitle = mTitle
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)
        Glide.with(this).load(mPoster).into(imageView)
        rating_tv.text = java.lang.Double.toString(mRating)
        title_tv.text = mTitle
        overview_tv.text = mOverView
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}