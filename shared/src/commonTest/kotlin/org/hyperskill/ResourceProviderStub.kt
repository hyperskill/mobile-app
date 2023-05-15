package org.hyperskill

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.core.view.mapper.ResourceProvider

class ResourceProviderStub : ResourceProvider {
    override fun getString(stringResource: StringResource): String =
        ""

    override fun getString(stringResource: StringResource, vararg args: Any): String =
        ""

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int): String =
        ""

    override fun getQuantityString(pluralsResource: PluralsResource, quantity: Int, vararg args: Any): String =
        ""
}