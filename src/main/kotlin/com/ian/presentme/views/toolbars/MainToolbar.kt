package com.ian.presentme.views.toolbars

import com.ian.presentme.views.CreateSongView
import com.ian.presentme.views.PresentationView
import javafx.scene.control.Button
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

    private var isLive = false
    private var liveView = PresentationView()

    init {
        main_toolbar_add_song.action { openCreateSongView() }
        main_toolbar_start.action { toggleLiveView() }
        main_toolbar_start.text = START
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
            val stage = liveView.openWindow(stageStyle = StageStyle.UNDECORATED)
            if (Screen.getScreens().size > 1) {
                val displayBounds = Screen.getScreens()[1].bounds
                stage?.let {
                    it.x = displayBounds.minX
                    it.y = displayBounds.minY
                    it.width = displayBounds.width
                    it.height = displayBounds.height
                    it.isFullScreen = true
                    it.isFocused = true
                }
            } else {
                liveView.openWindow()
            }
            liveView.currentStage?.isAlwaysOnTop = true
            isLive = true
            main_toolbar_start.text = STOP
        }
    }
}