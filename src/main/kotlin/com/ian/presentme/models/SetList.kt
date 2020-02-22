package com.ian.presentme.models

data class SetList(val id: Int, val title: String) {
    val songIds: MutableList<Int> = mutableListOf()

    override fun toString(): String {
        return title
    }
}