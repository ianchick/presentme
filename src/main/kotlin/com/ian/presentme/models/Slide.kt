package com.ian.presentme.models

import javafx.scene.layout.BackgroundFill

data class Slide(var content: String = "") {
    var fontSize: Int? = null
    var fontFamily: String? = null
    var background: BackgroundFill? = null
}