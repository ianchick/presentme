package com.ian.presentme.app

import com.ian.presentme.view.MainView
import tornadofx.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MyApp: App(MainView::class, Styles::class) {
    companion object {
        const val PREFERENCES = "preferences.properties"
        const val DEFAULT_SONGS_DIR = "songs"
        const val DEFAULT_BACKGROUNDS_DIR = "backgrounds"
    }

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
        val preferencesFile = File(PREFERENCES)
        if (!preferencesFile.exists()) {
            val createdPreferences = preferencesFile.createNewFile()
            if (createdPreferences) {
                resetDefaultPreferences()
            }
        }
    }

    /**
     * Reset preferences back to default values
     */
    private fun resetDefaultPreferences() {
        val outputStream = FileOutputStream(PREFERENCES)
        val defaultProperties = Properties()
        defaultProperties["songs_dir"] = DEFAULT_SONGS_DIR
        defaultProperties["background_dir"] = DEFAULT_BACKGROUNDS_DIR
        defaultProperties.store(outputStream, null)
        outputStream.close()
    }
}