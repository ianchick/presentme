package com.ian.presentme.views

import com.google.gson.Gson
import com.ian.presentme.app.MyApp
import com.ian.presentme.events.UpdateSongListEvent
import com.ian.presentme.models.Song
import javafx.scene.control.ListView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    private val main_top_wrapper: VBox by fxid()
    private val main_songs_list_view: ListView<Song> by fxid()

    init {
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)

        subscribe<UpdateSongListEvent>  {
            populateSongList()
        }

        // Populate song list when first opening the app
        populateSongList()
    }

    private fun populateSongList() {
        val songsList = mutableListOf<Song>()
        val songsDirectory = File(getPreferences()["songs_dir"].toString())
        if (songsDirectory.exists()) {
            songsDirectory.listFiles()?.let {
                it.forEach { file ->
                    val song = Gson().fromJson(file.readText(), Song::class.java)
                    songsList.add(song)
                }
            }
        }
        main_songs_list_view.items = songsList.observable().sorted()
    }

    /**
     * Read preferences.properties and returns the Properties object
     */
    private fun getPreferences(): Properties {
        val prop = Properties()
        val fileInputStream = FileInputStream(MyApp.PREFERENCES)
        prop.load(fileInputStream)
        fileInputStream.close()
        return prop
    }
}