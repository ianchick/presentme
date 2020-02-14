package com.ian.presentme.views

import com.ian.presentme.app.PresentMeApp
import com.ian.presentme.app.PresentMeApp.Companion.setPreference
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import tornadofx.*

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    private val main_top_wrapper: VBox by fxid()
    private val main_left_wrapper:  SplitPane  by fxid()
    private val main_center_split_pane_wrapper: SplitPane by fxid()
    private val main_right_split_pane: SplitPane by fxid()

    /**
     * Initialize toolbars and children views.
     * Interaction between views are done through event buses
     */
    init {
        // Initialize children views
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)
        main_left_wrapper.add(SongListView::class)
        main_left_wrapper.add(SetListListView::class)
        main_center_split_pane_wrapper.add(SlidesFlowView::class)
        main_center_split_pane_wrapper.add(BackgroundFlowView::class)
        main_right_split_pane.add(PresentationView::class)
        // Placeholder
        main_right_split_pane.add(VBox())
    }

    /**
     * On close request to set preferences
     */
    override fun onDock() {
        currentWindow?.let { window ->
            window.setOnCloseRequest {
                setPreference(PresentMeApp.WINDOW_SIZE_WIDTH, window.width.toString())
                setPreference(PresentMeApp.WINDOW_SIZE_HEIGHT, window.height.toString())

            }
        }
    }
}