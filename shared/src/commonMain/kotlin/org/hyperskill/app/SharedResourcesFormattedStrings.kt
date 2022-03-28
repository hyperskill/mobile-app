package org.hyperskill.app

import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

object SharedResourcesFormattedStrings {
    fun getFormattedTextExample(placeholder: String): StringDesc =
        StringDesc.ResourceFormatted(SharedResources.strings.shared_formatted_text_example, placeholder)
}