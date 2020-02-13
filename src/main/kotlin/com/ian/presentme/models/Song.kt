package com.ian.presentme.models

class Song(title: String, artist: String = "") {
    var slides: MutableList<Slide>? = null
}