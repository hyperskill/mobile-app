package org.hyperskill.providers

import org.hyperskill.app.providers.domain.model.Provider

fun Provider.Companion.stub(
    id: Long,
    title: String = "",
    description: String = ""
): Provider =
    Provider(
        id = id,
        title = title,
        description = description
    )