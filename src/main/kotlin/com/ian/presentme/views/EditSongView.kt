package com.ian.presentme.views

import com.ian.presentme.models.Song
import javafx.scene.layout.VBox
import tornadofx.*

class EditSongView(val song: Song): View() {
    override val root: VBox by fxml()
}