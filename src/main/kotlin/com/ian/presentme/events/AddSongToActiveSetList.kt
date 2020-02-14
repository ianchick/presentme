package com.ian.presentme.events

import com.ian.presentme.models.Song
import tornadofx.*

class AddSongToActiveSetList(val song: Song): FXEvent()