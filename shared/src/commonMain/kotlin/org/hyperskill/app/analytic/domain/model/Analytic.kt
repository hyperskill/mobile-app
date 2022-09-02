package org.hyperskill.app.analytic.domain.model

interface Analytic {
    fun reportEvent(event: AnalyticEvent)
}