package org.hyperskill.app.welcome_onboarding.track_details.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class WelcomeOnboardingTrackDetailsComponentImpl(
    private val track: WelcomeOnboardingTrack,
    private val appGraph: AppGraph
) : WelcomeOnboardingTrackDetailsComponent {
    override val welcomeOnboardingTrackDetailsFeature: Feature<ViewState, Message, Action>
        get() = WelcomeOnboardingTrackDetailsFeatureBuilder.build(
            track = track,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            profileRepository = appGraph.profileDataComponent.profileRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}