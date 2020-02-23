package com.ian.presentme.views

import com.ian.presentme.app.UserSession
import com.ian.presentme.app.Utilities
import com.ian.presentme.events.UpdateSetListEvent
import com.ian.presentme.models.SetList
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

class CreateSetListView: View() {
    override val root: VBox by fxml()
    val create_set_view_save: Button by fxid()
    val create_set_view_cancel: Button by fxid()
    val create_set_view_title: TextField by fxid()

    init {
        create_set_view_save.action {
            val title = create_set_view_title.text
            val setList = SetList(Utilities.generateSetId(), title)
            UserSession.addSet(setList)
            fire(UpdateSetListEvent(setList))
            close()
        }

        create_set_view_cancel.action {
            close()
        }
    }
}