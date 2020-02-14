package com.ian.presentme.events

import com.ian.presentme.models.Song
import tornadofx.*

/**
 * Send event to update the song list
 */
class UpdateSongListEvent(val song: Song?): FXEvent()