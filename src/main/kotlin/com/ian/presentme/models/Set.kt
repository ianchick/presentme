package com.ian.presentme.models

data class Set(val title: String) {
    val songsList: MutableList<Song>? = null
    val slidesList: MutableList<Slide>? = null
}