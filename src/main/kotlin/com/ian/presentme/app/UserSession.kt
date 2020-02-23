package com.ian.presentme.app

import com.ian.presentme.app.PreferenceController.Companion.SET_IDS
import com.ian.presentme.app.PreferenceController.Companion.SONG_IDS
import com.ian.presentme.models.SetDatabase
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song
import com.ian.presentme.models.SongDatabase
import java.util.regex.Pattern

object UserSession {
    val songDB = SongDatabase()
    val setlistDB = SetDatabase()
    val songIds = mutableListOf<Int>()
    val setIds = mutableListOf<Int>()

    private val fc = FileStorageController()
    private val pc = PreferenceController()

    fun initialize() {
        val songs = fc.getSongFiles()
        songs.forEach { song ->
            songDB.addSong(song)
        }
        val setlists = fc.getSetlistFiles()
        setlists.forEach { set ->
            setlistDB.setSetList(set.id, set)
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
        setlistDB.setSetList(setList.id, setList)
        setIds.add(setList.id)
    }

    fun addSong(song: Song) {
        songDB.addSong(song)
        songIds.add(song.id)
    }

    fun updateFiles() {
        songDB.getValues().forEach {
            fc.saveSongFile(it)
        }
        setlistDB.getValues().forEach {
            fc.saveSetFile(it)
        }
    }
}