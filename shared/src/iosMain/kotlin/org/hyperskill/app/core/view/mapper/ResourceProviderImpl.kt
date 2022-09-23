package org.hyperskill.app.core.view.mapper

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Plural
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format

class ResourceProviderImpl : ResourceProvider {
    override fun getString(stringResource: StringResource): String =
        stringResource.desc().localized()

    override fun getString(stringResource: StringResource, vararg args: Any): String =
        stringResource.format(*args).localized()

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int): String =
        StringDesc.Plural(pluralsResource, quantity).localized()

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int, vararg args: Any): String =
        StringDesc.PluralFormatted(pluralsResource, quantity, *args).localized()
}