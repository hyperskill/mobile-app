package org.hyperskill.app.core.view.mapper

import android.content.Context
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Plural
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(stringResource: StringResource): String =
        stringResource.desc().toString(context)

    override fun getString(stringResource: StringResource, vararg args: Any): String =
        stringResource.format(*args).toString(context)

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int): String =
        StringDesc.Plural(pluralsResource, quantity).toString(context)

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int, vararg args: Any): String =
        StringDesc.PluralFormatted(pluralsResource, quantity, *args).toString(context)
}