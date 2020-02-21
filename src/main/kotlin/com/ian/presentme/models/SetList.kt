package com.ian.presentme.models

data class SetList(val id: Int, val title: String) {
    val songsList: MutableList<Song> = mutableListOf()
}