package com.ian.presentme.events

import tornadofx.*

/**
 * @param toShow true = show, false = hide
 */
class ToggleBackgroundViewEvent(val toShow: Boolean) : FXEvent()