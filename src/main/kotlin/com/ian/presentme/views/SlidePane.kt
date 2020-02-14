package com.ian.presentme.views

import com.ian.presentme.events.UpdatePresentationView
import javafx.scene.input.MouseButton
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import tornadofx.*

class SlidePane(val source: FlowPane): View() {
    override val root: StackPane by fxml()
    val slide_content: Text by fxid()

    init {
        root.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                fire(UpdatePresentationView(this))
            } else if (it.button == MouseButton.SECONDARY) {
                EditSlidePaneView().openWindow()
                println(source.children.indexOf(root))
            }
        }

    }
}