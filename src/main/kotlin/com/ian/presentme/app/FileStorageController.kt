package com.ian.presentme.app

import com.google.gson.GsonBuilder
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song
import java.io.File

class FileStorageController {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun getSongFiles(): List<Song> {
        val songsList = mutableListOf<Song>()
        val songsDirectory = File(PresentMeApp.getPreferences(PresentMeApp.SONGS_DIR_KEY))
        if (songsDirectory.exists()) {
            songsDirectory.listFiles()?.let {
                it.forEach { file ->
                    val song = gson.fromJson(file.readText(), Song::class.java)
                    songsList.add(song)
                }
            }
        }
        return songsList
    }

    fun saveSongFile(song: Song) {
        val jsonString = gson.toJson(song)
        val file = File(PresentMeApp.getPreferences(PresentMeApp.SONGS_DIR_KEY) + "/${song.title}")
        file.writeText(jsonString)
    }

    /**
     * Writes set to local storage file
     *
     * @param set Set object to write to file
     */
    fun saveSetFile(set: SetList) {
        val file = File(PresentMeApp.getPreferences(PresentMeApp.SETS_DIR_KEY)).resolve(set.title)
        val jsonString = gson.toJson(set)
        file.writeText(jsonString)
    }

    fun deleteSongFile(song: Song) {
        val songsDirectory = File(PresentMeApp.getPreferences(PresentMeApp.SONGS_DIR_KEY))
        songsDirectory.listFiles()?.let {
            it.forEach { file ->
                val songFromFile = gson.fromJson(file.readText(), Song::class.java)
                if (songFromFile.title == song.title) {
                    file.delete()
                }
            }
        }
    }
}