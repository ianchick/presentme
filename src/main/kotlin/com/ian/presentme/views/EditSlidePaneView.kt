package com.ian.presentme.views

import com.ian.presentme.app.FileStorageController
import com.ian.presentme.events.UpdateSlidesFlowViewEvent
import com.ian.presentme.models.SlideSource
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import tornadofx.*

class EditSlidePaneView(parent: SlidesFlowView, slidePane: SlidePane, source: SlideSource) : View() {
    override val root: VBox by fxml()
    private val edit_slide_pane_text: TextArea by fxid()
    private val edit_slide_pane_save: Button by fxid()
    private val edit_slide_pane_cancel: Button by fxid()

    init {
        edit_slide_pane_text.text = slidePane.slide_content.text

        edit_slide_pane_save.action {
            val fs = FileStorageController()
            val index = parent.slides_flow_pane.children.indexOf(slidePane.root)
            source.slides[index].content = edit_slide_pane_text.text
            fs.saveFile(source)
            close()
            fire(UpdateSlidesFlowViewEvent(source))
        }
        edit_slide_pane_cancel.action {
            close()
        }
    }
}