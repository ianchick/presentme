package com.ian.presentme.views

import com.google.gson.Gson
import com.ian.presentme.app.MyApp
import com.ian.presentme.app.Styles
import com.ian.presentme.events.UpdateSongListEvent
import com.ian.presentme.models.Slide
import com.ian.presentme.models.Song
import javafx.scene.control.ListView
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    private val main_top_wrapper: VBox by fxid()
    private val main_songs_list_view: ListView<Song> by fxid()
    private val main_slides_flow_pane: FlowPane by fxid()
    private val main_slides_scroll_wrapper: ScrollPane by fxid()

    /**
     * Initialize toolbars
     * Populate songs list on first open
     */
    init {
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)
        subscribe<UpdateSongListEvent>  {
            populateSongList()
        }

        setSongListEventListeners()

        // Flow pane listen for window resize
        main_slides_flow_pane.prefWrapLengthProperty().bind(main_slides_scroll_wrapper.widthProperty())

        // Populate song list when first opening the app
        populateSongList()
    }

    /**
     * Set song list event listeners
     * Item selection change listener
     * Double click listener
     * Delete listener
     */
    private fun setSongListEventListeners() {
        main_songs_list_view.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            newValue.slides?.let {
                populateSlidesView(it)
            }
        })
        // Double click
        main_songs_list_view.onUserSelect(2) {
            it.slides?.let { slides ->
                println("ADD TO SET LIST $it")
            }
        }
        // Hit delete / backspace
        main_songs_list_view.onUserDelete {
            println("DELETE SONG?")
        }
    }

    /**
     * Lists all files in songs_dir directory and deserializes json to song objects to be put into songs list
     */
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

    private fun populateSlidesView(slidesList: MutableList<Slide>)  {
        main_slides_flow_pane.clear()
        slidesList.forEach {
            val pane = SlidePane()
            pane.root.addClass(Styles.slidePane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = it.content
            main_slides_flow_pane.add(pane)
        }
    }
}