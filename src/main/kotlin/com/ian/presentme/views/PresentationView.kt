package com.ian.presentme.views

import com.ian.presentme.app.Styles
import com.ian.presentme.events.UpdatePresentationView
import javafx.scene.layout.StackPane
import tornadofx.*

class PresentationView: View() {
    override val root: StackPane by fxml()

    init {
        subscribe<UpdatePresentationView> { event ->
            val pane = SlidePane(event.slidePane.source)
            pane.root.addClass(Styles.slidePane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = event.slidePane.slide_content.text
            root.add(pane)
        }
    }
}