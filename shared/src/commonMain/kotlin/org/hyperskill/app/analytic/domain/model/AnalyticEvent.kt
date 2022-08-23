package org.hyperskill.app.analytic.domain.model

interface AnalyticEvent {
    val name: String
    val params: Map<String, Any>
        get() = emptyMap()

    val source: AnalyticSource
}