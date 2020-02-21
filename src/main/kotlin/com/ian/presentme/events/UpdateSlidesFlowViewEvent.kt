package com.ian.presentme.events

import com.ian.presentme.models.SlideSource
import tornadofx.*

class UpdateSlidesFlowViewEvent(val source: List<SlideSource>) : FXEvent()