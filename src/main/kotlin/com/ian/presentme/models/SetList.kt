package com.ian.presentme.models

data class SetList(val title: String) {
    val songsList: MutableList<Song> = mutableListOf()
    val slidesList: MutableList<Slide> = mutableListOf()

    /**
     * Clear slides and iterate through songsList to add slides back into slides list
     *
     */
    fun setSlidesFromSongs() {
        slidesList.clear()
        for (song in songsList) {
            song.slides?.let {
                slidesList.addAll(it)
            }
        }
    }
}