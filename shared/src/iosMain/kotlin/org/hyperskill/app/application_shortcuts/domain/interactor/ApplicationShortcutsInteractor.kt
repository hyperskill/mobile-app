package org.hyperskill.app.application_shortcuts.domain.interactor

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailData
import org.hyperskill.app.profile_settings.domain.model.FeedbackEmailDataBuilder

class ApplicationShortcutsInteractor(
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val platform: Platform,
    private val userAgentInfo: UserAgentInfo,
    private val resourceProvider: ResourceProvider
) {
    suspend fun getSendFeedbackEmailData(): FeedbackEmailData {
        val currentProfile = currentProfileStateRepository
            .getState()
            .getOrNull()

        return FeedbackEmailDataBuilder.build(
            supportEmail = resourceProvider.getString(SharedResources.strings.settings_send_feedback_support_email),
            applicationName = resourceProvider.getString(SharedResources.strings.app_name),
            platform = platform,
            userId = currentProfile?.id,
            applicationVersion = userAgentInfo.versionCode
        )
    }
}