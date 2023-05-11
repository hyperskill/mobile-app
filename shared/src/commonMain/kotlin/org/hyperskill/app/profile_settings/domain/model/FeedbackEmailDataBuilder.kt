package org.hyperskill.app.profile_settings.domain.model

import org.hyperskill.app.core.domain.Platform

object FeedbackEmailDataBuilder {
    fun build(
        supportEmail: String,
        applicationName: String,
        platform: Platform,
        userId: Long?,
        applicationVersion: String
    ): FeedbackEmailData {
        val subject = "[$applicationName] ${platform.feedbackName} Feedback"

        val body =
            "\n\n\n---\nUser ID: ${userId ?: "None"}\nApp version: $applicationVersion\nDevice: ${platform.platform}"

        return FeedbackEmailData(
            mailTo = supportEmail,
            subject = subject,
            body = body
        )
    }
}