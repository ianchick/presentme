package com.ian.presentme.models

data class SetList(val title: String): SlideSource() {
    val songsList: MutableList<Song> = mutableListOf()

    /**
     * Clear slides and iterate through songsList to add slides back into slides list
     */
    fun setSlidesFromSongs() {
        slides.clear()
        for (song in songsList) {
            song.slides.let {
                slides.addAll(it)
            }
        }
    }
}