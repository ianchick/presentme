package com.ian.presentme.views.toolbars

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.BACKGROUND_FLOW_SHOWN
import com.ian.presentme.app.UserSession
import com.ian.presentme.app.Utilities
import com.ian.presentme.events.ToggleBackgroundViewEvent
import com.ian.presentme.events.UpdateSongListEvent
import com.ian.presentme.models.Slide
import com.ian.presentme.models.Song
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.stage.FileChooser
import tornadofx.*

class MainMenuBar: View() {
    override val root: MenuBar by fxml()
    private val main_menu_view_bg: CheckMenuItem by fxid()
    private val main_menu_import_songs: MenuItem by fxid()
    private val main_menu_file_refresh: MenuItem by fxid()
    private val pc = PreferenceController()
    private val fc = FileStorageController()

    init {
        main_menu_view_bg.isSelected = pc.getPreferences(BACKGROUND_FLOW_SHOWN).toBoolean()
        main_menu_view_bg.action { toggleBackgroundsView() }
        main_menu_import_songs.action{ bulkImportSongs() }
        main_menu_file_refresh.action {
            fire(UpdateSongListEvent)
        }
    }

    private fun toggleBackgroundsView() {
        if (main_menu_view_bg.isSelected) {
            pc.setPreference(BACKGROUND_FLOW_SHOWN, "true")
            fire(ToggleBackgroundViewEvent(true))
        } else {
            pc.setPreference(BACKGROUND_FLOW_SHOWN, "false")
            fire(ToggleBackgroundViewEvent(false))
        }
    }

    private fun bulkImportSongs() {
        val files = chooseFile("Choose Files", mode = FileChooserMode.Multi, filters = arrayOf(FileChooser.ExtensionFilter("All Files", "*")))
        files.forEach { file ->
            val content = file.readText()
            val song = Song(Utilities.generateSongId(), file.nameWithoutExtension.replace("_", " "))
            content.split("\\n\\n").forEach {
                song.slides.add(Slide(it.replace("\\n", "\n")))
            }
            UserSession.songDB.setSong(song.id, song)
        }
        UserSession.updateFiles()
        fire(UpdateSongListEvent)
    }
}