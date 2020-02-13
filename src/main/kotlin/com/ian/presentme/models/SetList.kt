package com.ian.presentme.models

data class SetList(val title: String) {
    val songsList: MutableList<Song> = mutableListOf()
    val slidesList: MutableList<Slide> = mutableListOf()
}