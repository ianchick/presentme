package com.ian.presentme.views

import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import tornadofx.*

class MainView : View("PresentMe") {
    override val root: BorderPane by fxml()
    val main_top_wrapper: VBox by fxid()

    init {
        main_top_wrapper.add(MainMenuBar::class)
        main_top_wrapper.add(MainToolbar::class)
    }
}