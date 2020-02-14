package com.ian.presentme.views

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ian.presentme.app.PresentMeApp
import com.ian.presentme.events.*
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File

class SetListListView: View() {
    override val root: VBox by fxml()
    private val set_list_create_button: Button by fxid()
    private val set_list_listview: ListView<Song> by fxid()
    private val set_list_label: Label by fxid()

    // Currently active Set List
    private var activeSet: SetList? = null

    init {
        // Set set list event listeners
        setSetListEventListeners()

        // Create new set list action
        set_list_create_button.action {
            CreateSetListView().openWindow()
        }

        // Update the set list with the given list and set to active
        subscribe<UpdateSetListEvent> { event ->
            activeSet = event.setList
            PresentMeApp.setPreference(PresentMeApp.ACTIVE_SET, event.setList.title)
            set_list_label.text = event.setList.title
            populateSetList(event.setList)
        }

        subscribe<DeselectSetListItemEvent> {
            set_list_listview.selectionModel.select(null)
        }

        // Adds given song to the active set if active set is not null, and updates set list
        subscribe<AddSongToActiveSetList> { event ->
            val song = event.song
            song.slides?.let { slides ->
                activeSet?.let { set ->
                    set.slidesList.addAll(slides)
                    set.songsList.add(song)
                    writeToSetListFile(set)
                    fire(UpdateSetListEvent(set))
                }
            }
        }
        // Set active set list upon app start from preferences if previously saved
        val preferencesSavedSet = PresentMeApp.getPreferences(PresentMeApp.ACTIVE_SET)
        if (preferencesSavedSet.isNotEmpty()) {
            // If file exists for set name saved in preferences then populate
            val file = File(PresentMeApp.SETS_DIR_KEY).resolve(preferencesSavedSet)
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
     * Writes setlist to local storage file
     *
     * @param set Set object to write to file
     */
    private fun writeToSetListFile(set: SetList) {
        val file = File(PresentMeApp.getPreferences(PresentMeApp.SETS_DIR_KEY)).resolve(set.title)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(set)
        file.writeText(jsonString)
    }

    /**
     * Set list even listeners
     * Double click to remove, select to show all slides in set
     */
    private fun setSetListEventListeners() {
        set_list_listview.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            newValue?.let { song ->
                activeSet?.let {
                    fire(UpdateSlidesFlowViewEvent(it.slidesList))
                    fire(DeselectSongsListItemEvent)
                }
            }
        })
        set_list_listview.onUserSelect(2) {
            activeSet?.let {
                val index = set_list_listview.selectionModel.selectedIndexProperty().get()
                it.songsList.removeAt(index)
                it.setSlidesFromSongs()
                writeToSetListFile(it)
                fire(UpdateSetListEvent(it))
                fire(UpdateSlidesFlowViewEvent(it.slidesList))
            }
        }
    }
}