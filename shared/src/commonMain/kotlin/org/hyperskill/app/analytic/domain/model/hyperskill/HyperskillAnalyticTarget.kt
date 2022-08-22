package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticTarget(val targetName: String) {
    SEND("send"),
    ALLOW("allow"),
    DENY("deny")
}