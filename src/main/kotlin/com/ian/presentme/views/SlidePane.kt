package com.ian.presentme.views

import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import tornadofx.*

class SlidePane: View() {
    override val root: StackPane by fxml()
    val slide_content: Text by fxid()
}