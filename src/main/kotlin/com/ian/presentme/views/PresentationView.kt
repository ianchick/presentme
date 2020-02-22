package com.ian.presentme.views

import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.FONT_SIZE
import com.ian.presentme.app.Styles
import com.ian.presentme.events.ChangeFontSizeEvent
import com.ian.presentme.events.UpdatePresentationView
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import tornadofx.*

open class PresentationView: View() {
    companion object {
        private const val TRANSITION_SPEED = 1000.0
    }

    override val root: StackPane by fxml()
    val pc = PreferenceController()

    init {
        subscribe<UpdatePresentationView> { event ->
            val pane = SlidePane(event.slidePane.parent, event.slidePane.source)
            pane.root.addClass(Styles.presentationPane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = event.slidePane.slide_content.text
            pane.slide_content.font = Font(pc.getPreferences(PreferenceController.FONT_SIZE).toDouble())
            pane.root.opacity = 0.0
            if (root.children[0].opacity == 0.0) {
                root.children[0] = pane.root
                root.children[1].fade(Duration(TRANSITION_SPEED), opacity =  0.0)
                root.children[0].fade(Duration(TRANSITION_SPEED), opacity =  1.0)
            } else {
                root.children[1] = pane.root
                root.children[0].fade(Duration(TRANSITION_SPEED), opacity =  0.0)
                root.children[1].fade(Duration(TRANSITION_SPEED), opacity =  1.0)
            }
        }

        subscribe<ChangeFontSizeEvent> { event ->
            if (root.children.isNotEmpty()) {
                root.children[0]?.let {
                    it.getChildList()?.let { text ->
                        (text[0] as Text).font = Font(event.fontSize.toDouble())
                        pc.setPreference(FONT_SIZE, event.fontSize.toDouble().toString())
                    }
                }
            }
        }
    }
}