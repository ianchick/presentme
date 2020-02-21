package com.ian.presentme.app

import com.ian.presentme.app.PreferenceController.Companion.BACKGROUNDS_DIR_KEY
import com.ian.presentme.app.PreferenceController.Companion.SETS_DIR_KEY
import com.ian.presentme.app.PreferenceController.Companion.SONGS_DIR_KEY
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_WIDTH
import com.ian.presentme.views.MainView
import javafx.stage.Stage
import tornadofx.*
import java.io.File

class PresentMeApp: App(MainView::class, Styles::class) {
    private val pc = PreferenceController()

    init {
        pc.generatePreferenceProperties()
        generateDataFolders()
        UserSession.initialize()
    }

    override fun start(stage: Stage) {
        stage.width = pc.getPreferences(WINDOW_SIZE_WIDTH).toDouble()
        stage.height = pc.getPreferences(WINDOW_SIZE_HEIGHT).toDouble()
        super.start(stage)
    }

    /**
     * Generate songs folder and backgrounds folder if they don't already exist.
     */
    private fun generateDataFolders() {
        val songsDirectory = File(pc.getPreferences(SONGS_DIR_KEY))
        if (!songsDirectory.exists()) {
            songsDirectory.mkdir()
        }
        val backgroundsDirectory = File(pc.getPreferences(BACKGROUNDS_DIR_KEY))
        if (!backgroundsDirectory.exists()) {
            backgroundsDirectory.mkdir()
        }
        val setsDirectory = File(pc.getPreferences(SETS_DIR_KEY))
        if (!setsDirectory.exists()) {
            setsDirectory.mkdir()
        }
    }
}