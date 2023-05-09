package org.hyperskill.app.profile_settings.domain.model

data class FeedbackEmailData(
    val mailTo: String,
    val subject: String,
    val body: String
)