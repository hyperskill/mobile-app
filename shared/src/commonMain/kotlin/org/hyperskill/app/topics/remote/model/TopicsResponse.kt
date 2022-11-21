package org.hyperskill.app.topics.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.topics.domain.model.Topic

@Serializable
class TopicsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("topics")
    val topics: List<Topic>
) : MetaResponse