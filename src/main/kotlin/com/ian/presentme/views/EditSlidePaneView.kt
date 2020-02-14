package com.ian.presentme.views

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import tornadofx.*

class EditSlidePaneView : View() {
    override val root: VBox by fxml()
    private val edit_slide_pane_text: TextArea by fxid()
    private val edit_slide_pane_save: Button by fxid()
    private val edit_slide_pane_cancel: Button by fxid()

    init {
        edit_slide_pane_save.action {
            close()
        }
        edit_slide_pane_cancel.action {
            close()
        }
    }
}