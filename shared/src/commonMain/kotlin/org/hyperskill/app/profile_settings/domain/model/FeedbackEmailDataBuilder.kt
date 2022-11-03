package org.hyperskill.app.profile_settings.domain.model

import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.config.BuildKonfig

object FeedbackEmailDataBuilder {
    fun build(
        applicationName: String,
        platform: Platform,
        userId: Long?,
        applicationVersion: String
    ): FeedbackEmailData {
        val subject = "[$applicationName] ${platform.feedbackName} Feedback"

        val body = "\n\n\n---\nUser ID: ${userId ?: "None"}\nApp version: $applicationVersion\nDevice: ${platform.platform}"

        return FeedbackEmailData(
            mailTo = BuildKonfig.SUPPORT_EMAIL,
            subject = subject,
            body = body
        )
    }
}