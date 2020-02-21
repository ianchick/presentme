package com.ian.presentme.views

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.app.Styles
import com.ian.presentme.app.UserSession
import com.ian.presentme.app.Utilities
import com.ian.presentme.events.AddSongToSongListEvent
import com.ian.presentme.models.Slide
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

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
     * Serializes song as json object and saves to file and saves with title as given file name.
     * Validations added for title
     */
    private fun save() {
        // Stores the slides that will be added to the song
        val songSlidesList = mutableListOf<Slide>()
        // Validation of required fields
        val title = create_song_title.text
        if (title.isNullOrEmpty()) {
            create_song_title.addClass(Styles.error)
        } else {
            val lyrics = create_song_lyrics.text
            val splitLyricsByNewline = lyrics.split("\n\n")
            splitLyricsByNewline.forEach {
                songSlidesList.add(Slide(it))
            }
            // Create song, serialize, write to file
            val song = Song(Utilities.generateSongId(), title)
            UserSession.addSong(song)
            song.slides = songSlidesList
            val fs = FileStorageController()
            fs.saveSongFile(song)
            fire(AddSongToSongListEvent(song))
            close()
        }
    }
}