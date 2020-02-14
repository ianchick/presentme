package com.ian.presentme.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.Screen
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val SCREEN_WIDTH = Screen.getPrimary().visualBounds.width
        val SCREEN_HEIGHT = Screen.getPrimary().visualBounds.height
        val SCREEN_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT

        val heading by cssclass()
        val error by cssclass()
        val slidePane by cssclass()
        val slideContent by cssclass()
        val flowpane by cssclass()
        val presentationView by cssclass()

        // Generic Colors
        val labelTextColor = c("#999")
        val errorBorderColor = c("f00")
        val selectedColor = c("00cc00")
        // Slide Colors
        val slideBgColor = c("#000")
        val slideTextColor = c("#fff")
    }

    init {
        // List headers
        label and heading {
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
            textFill = labelTextColor
        }

        textField and error {
            borderColor += box(errorBorderColor, errorBorderColor, errorBorderColor, errorBorderColor)
        }

        slidePane {
            backgroundColor += slideBgColor
            padding = box(20.px)
            alignment = Pos.CENTER
            prefHeight = 150.px
            prefWidth = prefHeight * SCREEN_RATIO
        }

        slideContent {
            fill = slideTextColor
            textAlignment = TextAlignment.CENTER
        }

        flowpane {
            hgap = 10.px
            vgap = 10.px
        }

        presentationView {
            backgroundColor += slideBgColor
        }
    }
}