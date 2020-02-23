package com.ian.presentme.events

import com.ian.presentme.models.SetList
import tornadofx.*

class UpdateSetListEvent(val setList: SetList? = null): FXEvent()