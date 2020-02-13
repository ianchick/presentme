package com.ian.presentme.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()

        // Colors
        val labelTextColor = c("#999")
    }

    init {
        label and heading {
            padding = box(5.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
            textFill = labelTextColor
        }
    }
}