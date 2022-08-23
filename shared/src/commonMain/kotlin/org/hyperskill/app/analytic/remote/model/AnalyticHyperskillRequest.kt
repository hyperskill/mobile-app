package org.hyperskill.app.analytic.remote.model

import kotlinx.serialization.json.JsonElement
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.extension.toJsonElement

class AnalyticHyperskillRequest(
    val events: List<AnalyticEvent>
) {
    fun toJsonElement(): JsonElement =
        events.map { it.params }.toJsonElement()
}