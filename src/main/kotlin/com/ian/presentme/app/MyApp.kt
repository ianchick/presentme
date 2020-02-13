package com.ian.presentme.app

import com.ian.presentme.view.MainView
import tornadofx.*
import java.io.File
import java.util.*

class MyApp: App(MainView::class, Styles::class) {

    init {
        generateDataFolders()
        generatePreferenceProperties()
    }

    /**
     * Generate songs folder and backgrounds folder if they don't already exist.
     */
    private fun generateDataFolders() {
        val songsDirectory = File("songs")
        if (!songsDirectory.exists()) {
            songsDirectory.mkdir()
        }
        val backgroundsDirectory = File("backgrounds")
        if (!backgroundsDirectory.exists()) {
            backgroundsDirectory.mkdir()
        }
    }

    /**
     * Generates preferences.properties file if it doesn't already exist.
     */
    private fun generatePreferenceProperties() {
        val preferencesFile = File("preferences.properties")
        if (!preferencesFile.exists()) {
            val createdPreferences = preferencesFile.createNewFile()
            if (createdPreferences) {
                val defaultProperties = Properties()
                defaultProperties["songs_dir"] = "songs"
                defaultProperties["background_dir"] = "background"
            }
        }

    }
}