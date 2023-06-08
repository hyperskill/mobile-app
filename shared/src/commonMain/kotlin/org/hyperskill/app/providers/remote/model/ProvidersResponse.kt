package org.hyperskill.app.providers.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.providers.domain.model.Provider

@Serializable
class ProvidersResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("providers")
    val providers: List<Provider>
) : MetaResponse