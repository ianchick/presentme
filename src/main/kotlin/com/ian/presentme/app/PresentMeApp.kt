package com.ian.presentme.app

import com.ian.presentme.views.MainView
import javafx.stage.Stage
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class PresentMeApp: App(MainView::class, Styles::class) {
    companion object {
        const val PREFERENCES = "preferences.properties"
        const val SONGS_DIR_KEY = "songs"
        const val BACKGROUNDS_DIR_KEY = "backgrounds"
        const val SETS_DIR_KEY = "sets"
        const val ACTIVE_SET = "activeSet"
        const val WINDOW_SIZE_WIDTH = "windowSizeWidth"
        const val WINDOW_SIZE_HEIGHT = "windowSizeHeight"


        /**
         * Get preference.properties value of given key
         *
         * @param key Key for the desired property value
         * @return Property value from preference.properties
         */
        fun getPreferences(key: String): String {
            val prop = Properties()
            val fileInputStream = FileInputStream(PREFERENCES)
            prop.load(fileInputStream)
            fileInputStream.close()
            return prop[key].toString()
        }

        /**
         * Update property value
         *
         * @param key Key of property to update
         * @param newValue New value
         */
        fun setPreference(key: String, newValue: String) {
            val prop = Properties()
            val fileInputStream = FileInputStream(PREFERENCES)
            prop.load(fileInputStream)
            fileInputStream.close()

            val fileOutputStream = FileOutputStream(PREFERENCES)
            prop[key] = newValue
            prop.store(fileOutputStream, null)
            fileOutputStream.close()
        }
    }

    init {
        generatePreferenceProperties()
        generateDataFolders()
    }

    override fun start(stage: Stage) {
        stage.width = getPreferences(WINDOW_SIZE_WIDTH).toDouble()
        stage.height = getPreferences(WINDOW_SIZE_HEIGHT).toDouble()
        super.start(stage)
    }

    /**
     * Generate songs folder and backgrounds folder if they don't already exist.
     */
    private fun generateDataFolders() {
        val songsDirectory = File(getPreferences(SONGS_DIR_KEY))
        if (!songsDirectory.exists()) {
            songsDirectory.mkdir()
        }
        val backgroundsDirectory = File(getPreferences(BACKGROUNDS_DIR_KEY))
        if (!backgroundsDirectory.exists()) {
            backgroundsDirectory.mkdir()
        }
        val setsDirectory = File(getPreferences(SETS_DIR_KEY))
        if (!setsDirectory.exists()) {
            setsDirectory.mkdir()
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
        defaultProperties[SONGS_DIR_KEY] = SONGS_DIR_KEY
        defaultProperties[BACKGROUNDS_DIR_KEY] = BACKGROUNDS_DIR_KEY
        defaultProperties[SETS_DIR_KEY] = SETS_DIR_KEY
        defaultProperties[ACTIVE_SET] = ""
        defaultProperties[WINDOW_SIZE_WIDTH] = "800"
        defaultProperties[WINDOW_SIZE_HEIGHT] = "600"
        defaultProperties.store(outputStream, null)
        outputStream.close()
    }
}