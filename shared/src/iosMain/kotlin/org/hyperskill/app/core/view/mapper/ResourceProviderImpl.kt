package org.hyperskill.app.core.view.mapper

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format

class ResourceProviderImpl : ResourceProvider {
    override fun getString(stringResource: StringResource): String =
        stringResource.desc().localized()

    override fun getString(stringResource: StringResource, vararg args: Any): String =
        stringResource.format(*args).localized()
}