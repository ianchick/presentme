package com.ian.presentme.app

import java.util.*

object Utilities {
    private val random = Random()

    fun generateSongId(): Int {
        var id = 0
        while (id in UserSession.songIds) {
            id = random.nextInt()
        }
        return id
    }

    fun generateSetId(): Int {
        var id = 0
        while (id in UserSession.setIds) {
            id = random.nextInt()
        }
        return id
    }
}