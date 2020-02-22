package com.ian.presentme.models

data class Song(val id: Int, val title: String, val artist: String = "") {
    var slides = mutableListOf<Slide>()
    /**
     * For showing song title in the song list
     */
    override fun toString(): String {
        return title.capitalize()
    }
}