package org.hyperskill.app.code.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RunCodeResult(
    @SerialName("stdin")
    val stdin: String,
    @SerialName("stdout")
    val stdout: String
)