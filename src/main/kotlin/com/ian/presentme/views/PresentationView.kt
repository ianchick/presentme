package com.ian.presentme.views

import com.ian.presentme.app.Styles
import com.ian.presentme.events.ChangeFontSizeEvent
import com.ian.presentme.events.UpdatePresentationView
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import tornadofx.*

class PresentationView: View() {
    override val root: StackPane by fxml()

    init {
        subscribe<UpdatePresentationView> { event ->
            root.clear()
            val pane = SlidePane(event.slidePane.parent, event.slidePane.source)
            pane.root.addClass(Styles.slidePane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = event.slidePane.slide_content.text
            root.add(pane)
        }

        subscribe<ChangeFontSizeEvent> { event ->
            if (root.children.isNotEmpty()) {
                root.children[0]?.let {
                    it.getChildList()?.let { text ->
                        (text[0] as Text).font = Font(event.fontSize.toDouble())
                    }
                }
            }
        }
    }
}