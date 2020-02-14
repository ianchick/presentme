package com.ian.presentme.views

import com.ian.presentme.app.Styles
import com.ian.presentme.events.UpdateSlidesFlowViewEvent
import com.ian.presentme.models.Slide
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import tornadofx.*

class SlidesFlowView : View() {
    override val root: ScrollPane by fxml()
    private val slides_flow_pane: FlowPane by fxid()
    private val slides_scroll_wrapper: ScrollPane by fxid()

    init {
        // Flow pane listen for window resize and focus traversable
        slides_flow_pane.prefWrapLengthProperty().bind(slides_scroll_wrapper.widthProperty())
        slides_flow_pane.isFocusTraversable = true

        subscribe<UpdateSlidesFlowViewEvent> { event ->
            populateSlidesView(event.slides)
        }
    }

    /**
     * Populates slides in slide flow pane
     *
     * @param slidesList List of slides to show in flow pane
     */
    private fun populateSlidesView(slidesList: MutableList<Slide>)  {
        slides_flow_pane.clear()
        slidesList.forEach {
            val pane = SlidePane()
            pane.root.addClass(Styles.slidePane)
            pane.slide_content.addClass(Styles.slideContent)
            pane.slide_content.text = it.content
            slides_flow_pane.add(pane)
        }
    }
}