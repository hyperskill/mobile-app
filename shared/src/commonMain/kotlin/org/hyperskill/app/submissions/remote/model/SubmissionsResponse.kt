package org.hyperskill.app.submissions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.submissions.domain.model.Submission

@Serializable
class SubmissionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("submissions")
    val submissions: List<Submission>
) : MetaResponse