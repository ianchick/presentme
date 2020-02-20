package com.ian.presentme.views

import com.ian.presentme.events.UpdatePresentationView
import com.ian.presentme.models.SlideSource
import javafx.scene.input.MouseButton
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import tornadofx.*

class SlidePane(val source: SlideSource): View() {
    override val root: StackPane by fxml()
    val slide_content: Text by fxid()

    init {
        root.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                fire(UpdatePresentationView(this))
            } else if (it.button == MouseButton.SECONDARY) {
                EditSlidePaneView().openWindow()
            }
        }

    }
}