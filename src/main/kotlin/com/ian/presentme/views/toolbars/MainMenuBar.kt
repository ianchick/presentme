package com.ian.presentme.views.toolbars

import com.ian.presentme.app.PreferenceController
import com.ian.presentme.app.PreferenceController.Companion.BACKGROUND_FLOW_SHOWN
import com.ian.presentme.events.ToggleBackgroundViewEvent
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuBar
import tornadofx.*

class MainMenuBar: View() {
    override val root: MenuBar by fxml()
    private val main_menu_view_bg: CheckMenuItem by fxid()
    private val pc = PreferenceController()

    init {
        main_menu_view_bg.isSelected = pc.getPreferences(BACKGROUND_FLOW_SHOWN).toBoolean()
        main_menu_view_bg.action { toggleBackgroundsView() }
    }

    private fun toggleBackgroundsView() {
        if (main_menu_view_bg.isSelected) {
            pc.setPreference(BACKGROUND_FLOW_SHOWN, "true")
            fire(ToggleBackgroundViewEvent(true))
        } else {
            pc.setPreference(BACKGROUND_FLOW_SHOWN, "false")
            fire(ToggleBackgroundViewEvent(false))
        }
    }
}