package org.hyperskill.app.profile_settings.injection

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsActionDispatcher
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsReducer
import org.hyperskill.app.profile_settings.view.ProfileSettingsViewStateMapper
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProfileSettingsFeatureBuilder {
    private const val LOG_TAG = "ProfileSettingsFeature"

    fun build(
        profileSettingsInteractor: ProfileSettingsInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        platform: Platform,
        userAgentInfo: UserAgentInfo,
        resourceProvider: ResourceProvider,
        urlPathProcessor: UrlPathProcessor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        purchaseInteractor: PurchaseInteractor,
        sentryInteractor: SentryInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val profileSettingsReducer = ProfileSettingsReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)
        val profileSettingsActionDispatcher = ProfileSettingsActionDispatcher(
            config = ActionDispatcherOptions(),
            profileSettingsInteractor = profileSettingsInteractor,
            currentProfileStateRepository = currentProfileStateRepository,
            analyticInteractor = analyticInteractor,
            authorizationFlow = authorizationFlow,
            platform = platform,
            userAgentInfo = userAgentInfo,
            resourceProvider = resourceProvider,
            urlPathProcessor = urlPathProcessor,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            purchaseInteractor = purchaseInteractor,
            sentryInteractor = sentryInteractor,
            logger = logger.withTag(LOG_TAG)
        )

        val viewStateMapper = ProfileSettingsViewStateMapper(resourceProvider)

        return ReduxFeature(State.Idle, profileSettingsReducer)
            .wrapWithActionDispatcher(profileSettingsActionDispatcher)
            .transformState(viewStateMapper::map)
    }
}