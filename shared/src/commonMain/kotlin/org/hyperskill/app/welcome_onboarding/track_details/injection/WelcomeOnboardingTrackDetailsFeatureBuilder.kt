package org.hyperskill.app.welcome_onboarding.track_details.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsActionDispatcher
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.ViewState
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsReducer
import org.hyperskill.app.welcome_onboarding.track_details.view.WelcomeOnboardingTrackDetailsViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object WelcomeOnboardingTrackDetailsFeatureBuilder {
    private const val LOG_TAG = "WelcomeOnboardingTrackDetailsFeature"

    fun build(
        track: WelcomeOnboardingTrack,
        currentProfileStateRepository: CurrentProfileStateRepository,
        profileRepository: ProfileRepository,
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val welcomeOnboardingTrackDetailsReducer =
            WelcomeOnboardingTrackDetailsReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val welcomeOnboardingTrackDetailsActionDispatcher = WelcomeOnboardingTrackDetailsActionDispatcher(
            ActionDispatcherOptions(),
            profileRepository = profileRepository,
            currentProfileStateRepository = currentProfileStateRepository,
            logger = logger.withTag(LOG_TAG)
        )

        val viewStateMapper = WelcomeOnboardingTrackDetailsViewStateMapper(resourceProvider)

        return ReduxFeature(
            initialState = WelcomeOnboardingTrackDetailsFeature.initialState(track),
            reducer = welcomeOnboardingTrackDetailsReducer
        )
            .wrapWithActionDispatcher(welcomeOnboardingTrackDetailsActionDispatcher)
            .transformState(viewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.event
            }
    }
}