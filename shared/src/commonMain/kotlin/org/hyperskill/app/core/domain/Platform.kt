package org.hyperskill.app.core.domain

expect class Platform() {
    val platform: String

    val isIos: Boolean
    val isAndroid: Boolean

    val analyticName: String

    val feedbackName: String
}