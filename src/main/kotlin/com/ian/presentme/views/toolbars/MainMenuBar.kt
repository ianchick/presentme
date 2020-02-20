package com.ian.presentme.views.toolbars

import com.ian.presentme.events.ToggleBackgroundViewEvent
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.MenuBar
import tornadofx.*

class MainMenuBar: View() {
    override val root: MenuBar by fxml()
    private val main_menu_view_bg: CheckMenuItem by fxid()

    init {
        main_menu_view_bg.isSelected = true
        main_menu_view_bg.action { toggleBackgroundsView() }
    }

    private fun toggleBackgroundsView() {
        if (main_menu_view_bg.isSelected) {
            fire(ToggleBackgroundViewEvent(true))
        } else {
            fire(ToggleBackgroundViewEvent(false))
        }
    }
}