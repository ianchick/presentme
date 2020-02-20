package com.ian.presentme.views

import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.CENTER_SP_DIV_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_WIDTH
import com.ian.presentme.events.ToggleBackgroundViewEvent
import com.ian.presentme.views.toolbars.MainMenuBar
import com.ian.presentme.views.toolbars.MainToolbar
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

    private val pc = PreferenceController()

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
        main_center_split_pane_wrapper.setDividerPosition(0, pc.getPreferences(CENTER_SP_DIV_HEIGHT).toDouble())
        main_right_split_pane.add(PresentationView::class)
        // Placeholder
        main_right_split_pane.add(VBox())

        // Hide the second child of center pane (backgrounds view)
        subscribe<ToggleBackgroundViewEvent> { event ->
            if (event.toShow) {
                main_center_split_pane_wrapper.add(BackgroundFlowView::class)
                main_center_split_pane_wrapper.setDividerPosition(0, pc.getPreferences(CENTER_SP_DIV_HEIGHT).toDouble())
            } else {
                pc.setPreference(CENTER_SP_DIV_HEIGHT, main_center_split_pane_wrapper.dividerPositions[0].toString())
                val backgroundFlowView = main_center_split_pane_wrapper.items[1]
                main_center_split_pane_wrapper.items.remove(backgroundFlowView)
            }
        }
    }

    /**
     * On close request to set preferences
     */
    override fun onDock() {
        currentWindow?.let { window ->
            window.setOnCloseRequest {
                pc.setPreference(WINDOW_SIZE_WIDTH, window.width.toString())
                pc.setPreference(WINDOW_SIZE_HEIGHT, window.height.toString())
                pc.setPreference(CENTER_SP_DIV_HEIGHT, main_center_split_pane_wrapper.dividerPositions[0].toString())
            }
        }
    }
}