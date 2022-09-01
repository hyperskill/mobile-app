package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.Platform
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailData

class FeedbackEmailDataBuilder(
    private val applicationName: String,
    private val platform: Platform,
    private val userId: Long?,
    private val applicationVersion: String
) {
    fun build(): FeedbackEmailData {
        val subject = "[$applicationName] ${platform.feedbackName} Feedback"

        val body = "\n\n\n---\nUser ID: $userId\nApp version: $applicationVersion\nDevice: ${platform.platform}"

        return FeedbackEmailData(
            mailTo = BuildKonfig.SUPPORT_EMAIL,
            subject = subject,
            body = body
        )
    }
}