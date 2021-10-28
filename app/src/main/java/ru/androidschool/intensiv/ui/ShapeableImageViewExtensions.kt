package ru.androidschool.intensiv.ui

import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

fun ShapeableImageView.loadImage(imageUrl: String?) {
    imageUrl?.let {
        Picasso.get()
            .load(imageUrl)
            .into(this)
    }
}