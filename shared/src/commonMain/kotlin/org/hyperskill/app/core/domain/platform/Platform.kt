package org.hyperskill.app.core.domain.platform

expect class Platform() {
    val platformType: PlatformType
    val platformDescription: String

    val analyticName: String

    val feedbackName: String
}