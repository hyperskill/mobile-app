package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.presentation.ProfileFeature
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileComponentImpl(private val appGraph: AppGraph) : ProfileComponent {
    override val profileFeature: Feature<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action>
        get() = ProfileFeatureBuilder.build(
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildProfileDataComponent().currentProfileStateRepository,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.buildProductsDataComponent().productsInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildNotificationComponent().notificationInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            appGraph.streakFlowDataComponent.streakFlow,
            appGraph.notificationFlowDataComponent.dailyStudyRemindersEnabledFlow
        )
}