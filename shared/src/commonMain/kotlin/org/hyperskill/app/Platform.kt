package org.hyperskill.app

expect class Platform() {
    val platform: String

    val analyticName: String

    val feedbackName: String
}