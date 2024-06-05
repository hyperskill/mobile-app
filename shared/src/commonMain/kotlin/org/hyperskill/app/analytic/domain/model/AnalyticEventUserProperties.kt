package org.hyperskill.app.analytic.domain.model

data class AnalyticEventUserProperties(
    val userId: Long?,
    val properties: Map<String, Any>
)