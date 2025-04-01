package com.devid_academy.cocktailbook.utils

import android.widget.ImageView
import com.devid_academy.cocktailbook.R
import com.squareup.picasso.Picasso

fun picassoInsert(url: String, imageView: ImageView) {
    Picasso.get()
        .load(url)
        .error(R.drawable.ic_launcher_background)
        .into(imageView)
}