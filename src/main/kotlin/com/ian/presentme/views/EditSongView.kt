package com.ian.presentme.views

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.app.Styles
import com.ian.presentme.app.UserSession
import com.ian.presentme.events.UpdateSlidesFlowViewEvent
import com.ian.presentme.models.Slide
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

class EditSongView(val song: Song): View() {
    override val root: VBox by fxml()
    private val edit_song_title: TextField by fxid()
    private val edit_song_artist: TextField by fxid()
    private val edit_song_lyrics: TextArea by fxid()
    private val edit_song_save: Button by fxid()

    init {
        edit_song_title.text = song.title
        edit_song_artist.text = song.artist
        val sb = StringBuilder()
        song.slides.forEach {
            sb.append("${it.content}\n\n")
        }
        edit_song_lyrics.text = sb.toString().trim()

        edit_song_save.action {
            val title = edit_song_title
            if (title.text.isEmpty()) {
                title.addClass(Styles.error)
            } else {
                val songSlidesList = mutableListOf<Slide>()
                val lyrics = edit_song_lyrics.text
                val splitLyricsByNewline = lyrics.split("\n\n")
                splitLyricsByNewline.forEach {
                    songSlidesList.add(Slide(it))
                }
                // Create song, serialize, write to file
                UserSession.addSong(song)
                song.slides = songSlidesList
                song.artist = edit_song_artist.text
                val fs = FileStorageController()
                fs.saveSongFile(song)
                close()
                fire(UpdateSlidesFlowViewEvent(listOf(song)))
            }
        }
    }
}