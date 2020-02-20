package com.ian.presentme.views

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.events.*
import com.ian.presentme.models.Song
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import tornadofx.*

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
                fire(UpdateSlidesFlowViewEvent(song))
                fire(DeselectSetListItemEvent)
            }
        })
        // Double click to add song to set list
        songlist_listview.onUserSelect(2) { song ->
            fire(AddSongToActiveSetList(song))
        }
        // Hit delete / backspace to delete song file
        songlist_listview.onUserDelete { song ->
            alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete ${songlist_listview.selectionModel.selectedItem}?",
                    "") { button ->
                if (button == ButtonType.OK) {
                    val fs = FileStorageController()
                    fs.deleteSongFile(song)
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
        val fs = FileStorageController()
        songlist_listview.items = fs.getSongFiles().observable().sorted()
    }
}