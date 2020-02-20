package com.ian.presentme.views

import com.ian.presentme.app.Styles
import com.ian.presentme.events.UpdateSlidesFlowViewEvent
import com.ian.presentme.models.SlideSource
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import tornadofx.*

class SlidesFlowView : View() {
    override val root: ScrollPane by fxml()
    val slides_flow_pane: FlowPane by fxid()
    private val slides_scroll_wrapper: ScrollPane by fxid()

    init {
        // Flow pane listen for window resize and focus traversable
        slides_flow_pane.prefWrapLengthProperty().bind(slides_scroll_wrapper.widthProperty())
        slides_flow_pane.isFocusTraversable = true

        subscribe<UpdateSlidesFlowViewEvent> { event ->
            populateSlidesView(event.source)
        }
    }

    /**
     * Populates slides in slide flow pane
     *
     * @param source Source of slides
     */
    private fun populateSlidesView(source: SlideSource)  {
        slides_flow_pane.clear()
        val slides = source.slides
        slides.forEach { slide ->
            val pane = SlidePane(this, source)
            pane.root.addClass(Styles.slidePane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = slide.content
            slides_flow_pane.add(pane)
        }
    }
}