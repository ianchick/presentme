package com.ian.presentme.views.toolbars

import com.ian.presentme.events.ChangeFontSizeEvent
import com.ian.presentme.views.CreateSongView
import com.ian.presentme.views.PresentationView
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ToolBar
import javafx.stage.Screen
import javafx.stage.StageStyle
import tornadofx.*

class MainToolbar: View() {
    companion object {
        private const val START = "Start"
        private const val STOP = "Stop"
    }

    override val root: ToolBar by fxml()
    private val main_toolbar_add_song: Button by fxid()
    private val main_toolbar_start: Button by fxid()
    private val main_toolbar_font_size: ComboBox<Int> by fxid()

    private var isLive = false
    private var liveView = PresentationView()

    init {
        main_toolbar_add_song.action { openCreateSongView() }
        main_toolbar_start.action { toggleLiveView() }
        main_toolbar_start.text = START
        populateFontSizeComboBox()

        main_toolbar_font_size.valueProperty().onChange {
            fire(ChangeFontSizeEvent(it!!))
        }
    }

    /**
     * Opens create song view window
     */
    private fun openCreateSongView() {
        CreateSongView().openModal()
    }

    private fun toggleLiveView() {
        if (isLive) { // Close
            isLive = false
            liveView.close()
            main_toolbar_start.text = START
        } else { // Open
            if (Screen.getScreens().size > 1) {
                val displayBounds = Screen.getScreens()[1].bounds
                val stage = liveView.openWindow(stageStyle = StageStyle.UNDECORATED)
                stage?.let {
                    it.x = displayBounds.minX
                    it.y = displayBounds.minY
                    it.width = displayBounds.width
                    it.height = displayBounds.height
                    it.isFullScreen = true
                }
            } else {
                liveView.openWindow()
            }
            liveView.currentStage?.isAlwaysOnTop = true
            isLive = true
            main_toolbar_start.text = STOP
        }
    }

    private fun populateFontSizeComboBox() {
        val fontSizes = observableList(12, 14, 18, 24, 30, 36, 48, 60, 72, 84)
        main_toolbar_font_size.items = fontSizes
    }
}