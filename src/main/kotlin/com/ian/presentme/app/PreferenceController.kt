package com.ian.presentme.app

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class PreferenceController {
    companion object {
        const val PREFERENCES = "preferences.properties"
        const val SONGS_DIR_KEY = "songs"
        const val BACKGROUNDS_DIR_KEY = "backgrounds"
        const val SETS_DIR_KEY = "sets"
        const val ACTIVE_SET = "activeSet"
        const val WINDOW_SIZE_WIDTH = "windowSizeWidth"
        const val WINDOW_SIZE_HEIGHT = "windowSizeHeight"
        const val CENTER_SP_DIV_HEIGHT = "centerSplitPaneDivHeight"
        const val FONT_SIZE = "fontSize"
        const val SONG_IDS = "songIds"
        const val SET_IDS =  "setIds"
        const val BACKGROUND_FLOW_SHOWN = "backgroundFlowShown"
        const val PREVIEW_VIEW_SHOWN = "previewViewShown"
        const val MAIN_LEFT_SP_DIV_POS = "mainLeftSplitPaneDivPosition"
        const val MAIN_RIGHT_SP_DIV_POS = "mainRightSplitPaneDivPosition"
    }

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
        if (prop[key] == null) {
            return ""
        }
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

    /**
     * Reset preferences back to default values
     */
    fun resetDefaultPreferences() {
        val outputStream = FileOutputStream(PREFERENCES)
        val defaultProperties = Properties()
        defaultProperties[SONGS_DIR_KEY] = SONGS_DIR_KEY
        defaultProperties[BACKGROUNDS_DIR_KEY] = BACKGROUNDS_DIR_KEY
        defaultProperties[SETS_DIR_KEY] = SETS_DIR_KEY
        defaultProperties[ACTIVE_SET] = ""
        defaultProperties[WINDOW_SIZE_WIDTH] = "800"
        defaultProperties[WINDOW_SIZE_HEIGHT] = "600"
        defaultProperties[CENTER_SP_DIV_HEIGHT] = "0.33"
        defaultProperties[FONT_SIZE] = "12.0"
        defaultProperties[SET_IDS] =  "0"
        defaultProperties[SONG_IDS] = "0"
        defaultProperties[BACKGROUND_FLOW_SHOWN] = "true"
        defaultProperties[PREVIEW_VIEW_SHOWN] = "true"
        defaultProperties[MAIN_RIGHT_SP_DIV_POS] = "0.66"
        defaultProperties.store(outputStream, null)
        outputStream.close()
    }

    /**
     * Generates preferences.properties file if it doesn't already exist.
     */
    fun generatePreferenceProperties() {
        val preferencesFile = File(PREFERENCES)
        if (!preferencesFile.exists()) {
            val createdPreferences = preferencesFile.createNewFile()
            if (createdPreferences) {
                resetDefaultPreferences()
            }
        }
    }
}

