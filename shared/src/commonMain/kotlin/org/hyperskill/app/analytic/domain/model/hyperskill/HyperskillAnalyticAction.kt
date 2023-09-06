package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticAction(val actionName: String) {
    CLICK("click"),
    VIEW("view"),
    HIDDEN("hidden"),
    SHOWN("shown"),
    ORIENTATION_CHANGED("screen_orientation_changed")
}