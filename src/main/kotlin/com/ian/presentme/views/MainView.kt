package com.ian.presentme.views

import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.BACKGROUND_FLOW_SHOWN
import com.ian.presentme.app.PreferenceController.Companion.CENTER_SP_DIV_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.MAIN_LEFT_SP_DIV_POS
import com.ian.presentme.app.PreferenceController.Companion.MAIN_RIGHT_SP_DIV_POS
import com.ian.presentme.app.PreferenceController.Companion.PREVIEW_DIV_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.PREVIEW_VIEW_SHOWN
import com.ian.presentme.app.PreferenceController.Companion.SET_IDS
import com.ian.presentme.app.PreferenceController.Companion.SONG_IDS
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_HEIGHT
import com.ian.presentme.app.PreferenceController.Companion.WINDOW_SIZE_WIDTH
import com.ian.presentme.app.UserSession
import com.ian.presentme.events.ToggleBackgroundViewEvent
import com.ian.presentme.events.TogglePreviewViewEvent
import com.ian.presentme.views.toolbars.MainMenuBar
import com.ian.presentme.views.toolbars.MainToolbar
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import tornadofx.*

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    private val main_top_wrapper: VBox by fxid()
    private val main_split_pane: SplitPane by fxid()
    private val main_left_wrapper:  SplitPane  by fxid()
    private val main_center_split_pane_wrapper: SplitPane by fxid()
    private val pc = PreferenceController()

    private val rightPane = SplitPane()

    /**
     * Initialize toolbars and children views.
     * Interaction between views are done through event buses
     */
    init {
        rightPane.orientation = Orientation.VERTICAL

        // Initialize children views
        if (pc.getPreferences(MAIN_LEFT_SP_DIV_POS).isNotBlank()) {
            main_split_pane.setDividerPosition(0, pc.getPreferences(MAIN_LEFT_SP_DIV_POS).toDouble())
        }
        if (pc.getPreferences(MAIN_RIGHT_SP_DIV_POS).isNotBlank()) {
            main_split_pane.setDividerPosition(1, pc.getPreferences(MAIN_RIGHT_SP_DIV_POS).toDouble())
        }
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)
        main_left_wrapper.add(SongListView::class)
        main_left_wrapper.add(SetListListView::class)
        main_center_split_pane_wrapper.add(SlidesFlowView::class)
        if (pc.getPreferences(BACKGROUND_FLOW_SHOWN).toBoolean()) {
            main_center_split_pane_wrapper.add(BackgroundFlowView::class)
        }
        main_center_split_pane_wrapper.setDividerPosition(0, pc.getPreferences(CENTER_SP_DIV_HEIGHT).toDouble())
        if (pc.getPreferences(PREVIEW_VIEW_SHOWN).toBoolean()) {
            rightPane.add(PreviewView::class)
            // Placeholder
            rightPane.add(VBox())
            main_split_pane.add(rightPane)
        }

        // Hide the second child of center pane (backgrounds view)
        subscribe<ToggleBackgroundViewEvent> { event ->
            toggleBackgroundFlowViewVisbility(event)
        }

        subscribe<TogglePreviewViewEvent> { event ->
            togglePreviewViewVisibility(event.toShow)
        }
    }

    private fun toggleBackgroundFlowViewVisbility(event: ToggleBackgroundViewEvent) {
        if (event.toShow) {
            main_center_split_pane_wrapper.add(BackgroundFlowView::class)
            main_center_split_pane_wrapper.setDividerPosition(0, pc.getPreferences(CENTER_SP_DIV_HEIGHT).toDouble())
        } else {
            pc.setPreference(CENTER_SP_DIV_HEIGHT, main_center_split_pane_wrapper.dividerPositions[0].toString())
            val backgroundFlowView = main_center_split_pane_wrapper.items[1]
            main_center_split_pane_wrapper.items.remove(backgroundFlowView)
        }
    }

    private fun togglePreviewViewVisibility(toShow: Boolean) {
        if (toShow) {
            main_split_pane.add(rightPane)
            if (rightPane.items.size == 0) {
                rightPane.add(PreviewView::class)
            }
            if (rightPane.items.size == 1) {
                rightPane.add(VBox())
            }
            // Placeholder
            main_split_pane.setDividerPosition(1, pc.getPreferences(PREVIEW_DIV_HEIGHT).toDouble())
        } else {
            pc.setPreference(PREVIEW_DIV_HEIGHT, main_split_pane.dividerPositions[1].toString())
            val previewView = main_split_pane.items[2]
            main_split_pane.items.remove(previewView)
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
                if (main_center_split_pane_wrapper.dividers.size > 0) {
                    pc.setPreference(CENTER_SP_DIV_HEIGHT, main_center_split_pane_wrapper.dividerPositions[0].toString())
                }
                if (main_split_pane.dividers.size > 0) {
                    pc.setPreference(MAIN_LEFT_SP_DIV_POS, main_split_pane.dividerPositions[0].toString())
                    if (main_split_pane.dividerPositions.size == 2) {
                        pc.setPreference(MAIN_RIGHT_SP_DIV_POS, main_split_pane.dividerPositions[1].toString())
                    }
                }
                pc.setPreference(SONG_IDS, UserSession.songIds.toString())
                pc.setPreference(SET_IDS, UserSession.setIds.toString())
                UserSession.updateFiles()
            }
        }
    }
}