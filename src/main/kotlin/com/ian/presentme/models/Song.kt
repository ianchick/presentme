package com.ian.presentme.models

data class Song(val id: Int, val title: String, val artist: String = "") : SlideSource() {
    /**
     * For showing song title in the song list
     */
    override fun toString(): String {
        return title.capitalize()
    }
}