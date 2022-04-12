package org.hyperskill.app

import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.StringDesc

object SharedResourcesFormatted {
    fun getStepTheoryTimeRemaining(minutes: Int): StringDesc =
        StringDesc.PluralFormatted(SharedResources.plurals.minutes, minutes, minutes)
}