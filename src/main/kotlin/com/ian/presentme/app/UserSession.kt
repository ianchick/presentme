package com.ian.presentme.app

import com.ian.presentme.app.PreferenceController.Companion.SET_IDS
import com.ian.presentme.app.PreferenceController.Companion.SONG_IDS
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song

object UserSession {
    val songDB = mutableMapOf<Int, Song>()
    val setlistDB = mutableMapOf<Int, SetList>()
    val songIds = mutableListOf<Int>()
    val setIds = mutableListOf<Int>()

    private val fc = FileStorageController()
    private val pc = PreferenceController()

    fun initialize() {
        val songs = fc.getSongFiles()
        songs.forEach { song ->
            songDB[song.id] = song
        }
        val setlists = fc.getSetlistFiles()
        setlists.forEach { set ->
            setlistDB[set.id] = set
        }
        pc.getPreferences(SONG_IDS).split(",").forEach { idString ->
            songIds.add(idString.toInt())
        }
        pc.getPreferences(SET_IDS).split(",").forEach { idString ->
            setIds.add(idString.toInt())
        }
    }
}