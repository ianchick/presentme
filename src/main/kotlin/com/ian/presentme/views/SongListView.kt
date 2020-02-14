package com.ian.presentme.views

import com.google.gson.Gson
import com.ian.presentme.app.PresentMeApp
import com.ian.presentme.events.*
import com.ian.presentme.models.Song
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File

class SongListView : View() {
    override val root: VBox by fxml()
    private val songlist_listview: ListView<Song> by fxid()

    init {
        // Set event listeners on song list items
        setSongListEventListeners()

        subscribe<DeselectSongsListItemEvent> {
            songlist_listview.selectionModel.select(null)
        }

        // Refresh song list from files and select new song
        // TODO: Assumption is that file of new song is already saved before event. Misleading
        subscribe<AddSongToSongListEvent> { event ->
            populateSongListFromLocalFiles()
            songlist_listview.selectionModel.select(event.song)
        }

        // Populate Song List on app open
        populateSongListFromLocalFiles()
    }

    /**
     * Set song list event listeners
     * Item selection change listener
     * Double click listener
     * Delete listener
     */
    private fun setSongListEventListeners() {
        songlist_listview.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            newValue?.let { song ->
                song.slides?.let {
                    fire(UpdateSlidesFlowViewEvent(it))
                    fire(DeselectSetListItemEvent)
                }
            }
        })
        // Double click to add song to set list
        songlist_listview.onUserSelect(2) { song ->
            fire(AddSongToActiveSetList(song))
        }
        // Hit delete / backspace to delete song file
        songlist_listview.onUserDelete { song ->
            val delete = alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete ${songlist_listview.selectionModel.selectedItem}?",
                    "") { button ->
                if (button == ButtonType.OK) {
                    val songsDirectory = File(PresentMeApp.getPreferences(PresentMeApp.SONGS_DIR_KEY))
                    songsDirectory.listFiles()?.let {
                        it.forEach { file ->
                            // TODO: This seems flaky if we ever change how files will be named.
                            if (file.name == song.title) {
                                file.delete()
                            }
                        }
                    }
                    populateSongListFromLocalFiles()
                    fire(DeselectSongsListItemEvent)
                }
            }
        }
    }

    /**
     * Populates song list with sorted list of songs pulled from local files
     */
    private fun populateSongListFromLocalFiles() {
        val songsList = mutableListOf<Song>()
        val songsDirectory = File(PresentMeApp.getPreferences(PresentMeApp.SONGS_DIR_KEY))
        if (songsDirectory.exists()) {
            songsDirectory.listFiles()?.let {
                it.forEach { file ->
                    val song = Gson().fromJson(file.readText(), Song::class.java)
                    songsList.add(song)
                }
            }
        }
        songlist_listview.items = songsList.observable().sorted()
    }
}