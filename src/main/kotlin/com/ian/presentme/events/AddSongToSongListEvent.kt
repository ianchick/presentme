package com.ian.presentme.events

import com.ian.presentme.models.Song
import tornadofx.*

class AddSongToSongListEvent(val song: Song) : FXEvent()