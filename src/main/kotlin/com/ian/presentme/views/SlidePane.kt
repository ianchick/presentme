package com.ian.presentme.views

import com.ian.presentme.events.UpdatePresentationView
import com.ian.presentme.models.Song
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import tornadofx.*

class SlidePane(val parent: SlidesFlowView, val source: Song): View() {
    override val root: StackPane by fxml()
    val slide_content: Text by fxid()

    init {
        root.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                fire(UpdatePresentationView(this))
                if (parent.activeSlide != null) {
                    parent.activeSlide!!.root.border = Border.EMPTY
                }
                root.border = Border(BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(5.0)))
                parent.activeSlide = this
            } else if (it.button == MouseButton.SECONDARY) {
                EditSlidePaneView(parent,this, source).openModal()
            }
        }
    }
}