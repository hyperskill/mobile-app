package org.hyperskill.app.sentry.domain.model.transaction

enum class HyperskillSentryTransactionOperation(val stringValue: String) {
    API_LOAD("api.load"),
    UI_LOAD("ui.load")
}