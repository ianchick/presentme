package com.ian.presentme.models

data class Song(val title: String, val artist: String = "") {
    var slides: MutableList<Slide>? = null

    /**
     * For showing song title in the song list
     */
    override fun toString(): String {
        return title.capitalize()
    }
}