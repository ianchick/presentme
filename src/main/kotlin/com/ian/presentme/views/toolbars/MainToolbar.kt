package com.ian.presentme.views.toolbars

import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.FONT_SIZE
import com.ian.presentme.events.ChangeFontSizeEvent
import com.ian.presentme.events.ClosePresentationViewEvent
import com.ian.presentme.events.EditCurrentSongEvent
import com.ian.presentme.events.ToggleEditSongButtonEvent
import com.ian.presentme.views.CreateSongView
import com.ian.presentme.views.PresentationView
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ToolBar
import javafx.stage.Modality
import javafx.stage.Screen
import javafx.stage.Stage
import tornadofx.*

class MainToolbar: View() {
    companion object {
        private const val START = "Start"
        private const val STOP = "Stop"
    }

    override val root: ToolBar by fxml()
    private val main_toolbar_add_song: Button by fxid()
    private val main_toolbar_start: Button by fxid()
    private val main_toolbar_font_size: ComboBox<String> by fxid()
    private val main_toolbar_edit_song: Button by fxid()

    private var isLive = false
    private val pc = PreferenceController()
    private var liveView = PresentationView()

    init {
        main_toolbar_edit_song.action { openEditSongView() }
        main_toolbar_add_song.action { openCreateSongView() }
        main_toolbar_start.action { toggleLiveView() }
        main_toolbar_start.text = START
        initFontComboBox()

        main_toolbar_font_size.valueProperty().onChange {
            pc.setPreference(FONT_SIZE, it.toString())
            fire(ChangeFontSizeEvent(it!!))
        }

        subscribe<ClosePresentationViewEvent> {
            toggleLiveView()
        }

        subscribe<ToggleEditSongButtonEvent> { event ->
            main_toolbar_edit_song.disableProperty().value = event.isDisabled
        }
    }

    /**
     * Opens create song view window
     */
    private fun openCreateSongView() {
        CreateSongView().openModal()
    }

    private fun openEditSongView() {
        fire(EditCurrentSongEvent)
    }

    private fun toggleLiveView() {
        if (isLive) { // Close
            isLive = false
            liveView.close()
            main_toolbar_start.text = START
        } else { // Open
            isLive = true
            main_toolbar_start.text = STOP
            if (Screen.getScreens().size > 1) {
                val displayBounds = Screen.getScreens()[1].bounds
                // Not sure why I need to set owner, but it works this way.
                val window = Stage()
                window.isAlwaysOnTop = true
                liveView.openModal(owner = window, modality = Modality.NONE)
                liveView.modalStage?.x = displayBounds.minX
                liveView.modalStage?.y = displayBounds.minY
                liveView.modalStage?.width = displayBounds.width
                liveView.modalStage?.height = displayBounds.height
            } else {
                liveView.openModal(modality = Modality.NONE)
            }
        }
    }

    private fun initFontComboBox() {
        val fontSizes = observableList("14", "18", "24", "30", "36", "48", "60", "72", "96", "120")
        main_toolbar_font_size.items = fontSizes
        main_toolbar_font_size.selectionModel.select(pc.getPreferences(FONT_SIZE))
    }
}