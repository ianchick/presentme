package com.ian.presentme.views

import com.google.gson.Gson
import com.ian.presentme.app.FileStorageController
import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.ACTIVE_SET
import com.ian.presentme.app.PreferenceController.Companion.SETS_DIR_KEY
import com.ian.presentme.events.*
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.stage.Modality
import tornadofx.*
import java.io.File

class SetListListView: View() {
    override val root: VBox by fxml()
    private val set_list_create_button: Button by fxid()
    private val set_list_listview: ListView<Song> by fxid()
    private val set_list_label: Label by fxid()

    // Currently active Set List
    private var activeSet: SetList? = null
    private val fs = FileStorageController()
    private val pc = PreferenceController()

    init {
        // Set set list event listeners
        setSetListEventListeners()

        // Create new set list action
        set_list_create_button.action {
            CreateSetListView().openWindow(modality = Modality.APPLICATION_MODAL)
        }

        // Update the set list with the given list and set to active
        subscribe<UpdateSetListEvent> { event ->
            activeSet = event.setList
            pc.setPreference(ACTIVE_SET, event.setList.title)
            set_list_label.text = event.setList.title
            populateSetList(event.setList)
        }

        subscribe<DeselectSetListItemEvent> {
            set_list_listview.selectionModel.select(null)
        }

        // Adds given song to the active set if active set is not null, and updates set list
        subscribe<AddSongToActiveSetList> { event ->
            val song = event.song
            song.slides.let { slides ->
                activeSet?.let { set ->
                    set.slides.addAll(slides)
                    set.songsList.add(song)
                    fs.saveSetFile(set)
                    fire(UpdateSetListEvent(set))
                }
            }
        }
        // Set active set list upon app start from preferences if previously saved
        val preferencesSavedSet = pc.getPreferences(ACTIVE_SET)
        if (preferencesSavedSet.isNotEmpty()) {
            // If file exists for set name saved in preferences then populate
            val file = File(pc.getPreferences(SETS_DIR_KEY)).resolve(preferencesSavedSet)
            if (file.exists()) {
                val set = Gson().fromJson(file.readText(), SetList::class.java)
                activeSet = set
                set_list_label.text = set.title
                populateSetList(set)
            }
        }
    }

    /**
     * Populate the set list with the list of songs
     *
     * @param setList List of songs to populate set list with
     */
    private fun populateSetList(setList: SetList) {
        set_list_listview.items = setList.songsList.observable()
    }

    /**
     * Set list even listeners
     * Double click to remove, select to show all slides in set
     */
    private fun setSetListEventListeners() {
        set_list_listview.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            // Uncaught IndexOutOfBounds exception if this null check isn't here
            newValue?.let {
                activeSet?.let { set ->
                    fire(UpdateSlidesFlowViewEvent(set))
                    fire(DeselectSongsListItemEvent)
                }
            }
        })
        set_list_listview.onUserSelect(2) {
            activeSet?.let {
                val index = set_list_listview.selectionModel.selectedIndexProperty().get()
                it.songsList.removeAt(index)
                it.setSlidesFromSongs()
                fs.saveSetFile(it)
                fire(UpdateSetListEvent(it))
                fire(UpdateSlidesFlowViewEvent(it))
            }
        }
    }
}