package com.ian.presentme.models

data class Song(val title: String, val artist: String = "") {
    var slides: MutableList<Slide>? = null
}