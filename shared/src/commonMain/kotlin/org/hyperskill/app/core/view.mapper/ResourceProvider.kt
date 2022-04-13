package org.hyperskill.app.core.view.mapper

import dev.icerock.moko.resources.StringResource

interface ResourceProvider {
    fun getString(stringResource: StringResource): String
    fun getString(stringResource: StringResource, vararg args: Any): String
}