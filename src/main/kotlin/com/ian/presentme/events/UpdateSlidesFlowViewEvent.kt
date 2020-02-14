package com.ian.presentme.events

import com.ian.presentme.models.Slide
import tornadofx.*

class UpdateSlidesFlowViewEvent(val slides: MutableList<Slide>) : FXEvent()