package com.ian.presentme.views

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import tornadofx.*

class MainToolbar: View() {
    override val root: ToolBar by fxml()
    val main_toolbar_add_song: Button by fxid()
    val main_toolbar_start: Button by fxid()

    init {
        main_toolbar_add_song.action { openCreateSongView() }
    }

    /**
     * Opens create song view window
     */
    private fun openCreateSongView() {
        CreateSongView().openWindow()
    }
}
