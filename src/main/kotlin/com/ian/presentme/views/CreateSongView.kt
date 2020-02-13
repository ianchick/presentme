package com.ian.presentme.views

import com.ian.presentme.events.UpdateSongListEvent
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File

class CreateSongView : View("Create New Song") {
    override val root: VBox by fxml()
    val create_song_title: TextField by fxid()
    val create_song_lyrics: TextArea by fxid()
    val create_song_save: Button by fxid()

    init {
        create_song_save.action {
            save()
        }
    }

    /**
     * Writes lyrics to file and saves with title as given file name.
     * TODO: Save author
     * TODO: Format
     */
    private fun save() {
        val title = create_song_title.text
        val lyrics = create_song_lyrics.text
        val song = Song(title)
        val file = File("songs/$title")
        file.writeText(lyrics)
        fire(UpdateSongListEvent())
    }
}