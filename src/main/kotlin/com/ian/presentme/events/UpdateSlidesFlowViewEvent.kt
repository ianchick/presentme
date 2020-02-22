package com.ian.presentme.events


import com.ian.presentme.models.Song
import tornadofx.*

class UpdateSlidesFlowViewEvent(val source: List<Song>) : FXEvent()