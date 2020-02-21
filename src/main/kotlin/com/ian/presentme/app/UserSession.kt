package com.ian.presentme.app

import com.ian.presentme.app.PreferenceController.Companion.SET_IDS
import com.ian.presentme.app.PreferenceController.Companion.SONG_IDS
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song
import java.util.regex.Pattern

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
        val digitsPattern = Pattern.compile("\\d+")
        val songMatches = digitsPattern.matcher(pc.getPreferences(SONG_IDS))
        while(songMatches.find()) {
            songIds.add(songMatches.group().toInt())
        }
        val setMatches = digitsPattern.matcher(pc.getPreferences(SET_IDS))
        while(setMatches.find()) {
            setIds.add(setMatches.group().toInt())
        }
    }

    fun addSet(setList: SetList) {
        setlistDB[setList.id] = setList
        setIds.add(setList.id)
    }

    fun addSong(song: Song) {
        songDB[song.id] = song
        songIds.add(song.id)
    }

    fun updateFiles() {
        songDB.forEach {
            fc.saveSongFile(it.value)
        }
        setlistDB.forEach {
            fc.saveSetFile(it.value)
        }
    }
}