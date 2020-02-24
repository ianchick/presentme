package com.ian.presentme.views

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.app.UserSession
import com.ian.presentme.events.UpdateSlidesFlowViewEvent
import com.ian.presentme.models.Song
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import tornadofx.*

class EditSlidePaneView(parent: SlidesFlowView, slidePane: SlidePane, source: Song) : View() {
    override val root: VBox by fxml()
    private val edit_slide_pane_text: TextArea by fxid()
    private val edit_slide_pane_save: Button by fxid()
    private val edit_slide_pane_cancel: Button by fxid()

    init {
        edit_slide_pane_text.text = slidePane.slide_content.text

        edit_slide_pane_save.action {
            save(parent, slidePane, source)
        }
        edit_slide_pane_cancel.action {
            close()
        }
    }

    private fun save(parent: SlidesFlowView, slidePane: SlidePane, source: Song) {
        val fs = FileStorageController()
        // find index of the slide in the song
        var songIndex = 0
        val flowPaneIndex = parent.slides_flow_pane.children.indexOf(slidePane.root)
        val songSlideSize = source.slides.size
        for (song in parent.songsSource) {
            // If song is the same and the index is smaller than the song slide size, than we are on the right instance of song
            if (song == source && flowPaneIndex - songIndex < songSlideSize) {
                break
            } else {
                songIndex += song.slides.size
            }
        }
        // Gets the slide's index in the song by subtracting real index from starting index of the song
        val index = flowPaneIndex - songIndex
        source.slides[index].content = edit_slide_pane_text.text
        UserSession.songDB.setSong(source.id, source)
        fs.saveSongFile(source)
        close()
        fire(UpdateSlidesFlowViewEvent(parent.songsSource))
    }
}