package com.ian.presentme.views

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ian.presentme.app.PresentMeApp
import com.ian.presentme.app.PresentMeApp.Companion.getPreferences
import com.ian.presentme.app.PresentMeApp.Companion.setPreference
import com.ian.presentme.app.Styles
import com.ian.presentme.events.UpdateSetListEvent
import com.ian.presentme.events.UpdateSongListEvent
import com.ian.presentme.models.SetList
import com.ian.presentme.models.Slide
import com.ian.presentme.models.Song
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    private val main_top_wrapper: VBox by fxid()
    private val main_songs_list_view: ListView<Song> by fxid()
    private val main_slides_flow_pane: FlowPane by fxid()
    private val main_slides_scroll_wrapper: ScrollPane by fxid()
    private val main_set_list_create: Button by fxid()
    private val main_set_list_view_label: Label by fxid()
    private val main_set_list_view: ListView<Song> by fxid()

    // Currently active Set List
    private var activeSet: SetList? = null

    /**
     * Initialize toolbars
     * Populate songs list on first open
     * Check for last active set and initialize active set listeners
     */
    init {
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)
        subscribe<UpdateSongListEvent> { event ->
            populateSongList()
            main_songs_list_view.selectionModel.select(event.song)
        }
        setSongListEventListeners()

        // Flow pane listen for window resize and focus traversable
        main_slides_flow_pane.prefWrapLengthProperty().bind(main_slides_scroll_wrapper.widthProperty())
        main_slides_flow_pane.isFocusTraversable = true

        // Populate song list when first opening the app
        populateSongList()

        // Active set list set on open and when update event fires.
        val activeSetName = getPreferences(PresentMeApp.ACTIVE_SET)
        if (activeSetName.isNotEmpty()) {
            val setFile = File(PresentMeApp.SETS_DIR_KEY).resolve(activeSetName)
            if (setFile.exists()) {
                activeSet = Gson().fromJson(setFile.readText(), SetList::class.java)
                main_set_list_view_label.text = activeSetName
                populateSlidesView(activeSet!!.slidesList)
                populateSetListSongsList(activeSet!!)
            }
        }
        subscribe<UpdateSetListEvent> { event ->
            activeSet = event.setList
            setPreference(PresentMeApp.ACTIVE_SET, event.setList.title)
            main_set_list_view_label.text = event.setList.title
            populateSlidesView(event.setList.slidesList)
            populateSetListSongsList(event.setList)
        }
        main_set_list_create.action {
            CreateSetListView().openWindow()
        }
        setSetListClickListeners()
    }

    /**
     * Set List click listeners
     * Double click to remove, select to show all slides in set
     *
     */
    private fun setSetListClickListeners() {
        main_set_list_view.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            newValue?.let { song ->
                activeSet?.let {
                    populateSlidesView(it.slidesList)
                    main_songs_list_view.selectionModel.select(null)
                }
            }
        })
        main_set_list_view.onUserSelect(2) {
            activeSet?.let {
                val index = main_set_list_view.selectionModel.selectedIndexProperty().get()
                it.songsList.removeAt(index)
                it.setSlidesFromSongs()
                populateSetListSongsList(it)
                populateSlidesView(it.slidesList)
            }
        }
    }

    /**
     * On close request to set preferences
     */
    override fun onDock() {
        currentWindow?.let { window ->
            window.setOnCloseRequest {
                setPreference(PresentMeApp.WINDOW_SIZE_WIDTH, window.width.toString())
                setPreference(PresentMeApp.WINDOW_SIZE_HEIGHT, window.height.toString())

            }
        }
    }

    /**
     * Set song list event listeners
     * Item selection change listener
     * Double click listener
     * Delete listener
     */
    private fun setSongListEventListeners() {
        main_songs_list_view.selectionModel.selectedItemProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            newValue?.let { song ->
                song.slides?.let {
                    populateSlidesView(it)
                    main_set_list_view.selectionModel.select(null)
                }
            }
        })
        // Double click
        main_songs_list_view.onUserSelect(2) { song ->
            song.slides?.let { slides ->
                activeSet?.let { set ->
                    set.slidesList.addAll(slides)
                    set.songsList.add(song)
                    writeToSetListFile(set)
                    populateSlidesView(set.slidesList)
                    populateSetListSongsList(set)
                }
            }
        }
        // Hit delete / backspace to remove song
        main_songs_list_view.onUserDelete { song ->
            val delete = alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete ${main_songs_list_view.selectionModel.selectedItem}?",
                    "") { button ->
                if (button == ButtonType.OK) {
                    val songsDirectory = File(getPreferences(PresentMeApp.SONGS_DIR_KEY))
                    songsDirectory.listFiles()?.let {
                        it.forEach { file ->
                            if (file.name == song.title) {
                                file.delete()
                            }
                        }
                    }
                    populateSongList()
                }
            }
        }
    }

    /**
     * Writes setlist to set file
     *
     * @param set Set object to write to file
     */
    private fun writeToSetListFile(set: SetList) {
        val file = File(getPreferences(PresentMeApp.SETS_DIR_KEY)).resolve(set.title)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(set)
        file.writeText(jsonString)
    }

    /**
     * Lists all files in songs_dir directory and deserializes json to song objects to be put into songs list
     */
    private fun populateSongList() {
        val songsList = mutableListOf<Song>()
        val songsDirectory = File(getPreferences(PresentMeApp.SONGS_DIR_KEY))
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
     * Populates slides in slide flow pane
     *
     * @param slidesList List of slides to show in flow pane
     */
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

    /**
     * Populate the set list with the list of songs
     *
     * @param setList Set list to populate set list list view
     */
    private fun populateSetListSongsList(setList: SetList) {
        main_set_list_view.items = setList.songsList.observable()
    }
}