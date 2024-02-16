package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.view.BadgesViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

internal class ProfileComponentImpl(
    private val appGraph: AppGraph
) : ProfileComponent {
    override val profileFeature: Feature<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action>
        get() = ProfileFeatureBuilder.build(
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            streaksInteractor = appGraph.buildStreaksDataComponent().streaksInteractor,
            productsInteractor = appGraph.buildProductsDataComponent().productsInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor,
            urlPathProcessor = appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            streakFlow = appGraph.streakFlowDataComponent.streakFlow,
            dailyStudyRemindersEnabledFlow = appGraph.notificationFlowDataComponent.dailyStudyRemindersEnabledFlow,
            submissionRepository = appGraph.submissionDataComponent.submissionRepository,
            badgesRepository = appGraph.buildBadgesDataComponent().badgesRepository,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )

    override val badgesViewStateMapper: BadgesViewStateMapper
        get() = BadgesViewStateMapper(appGraph.commonComponent.resourceProvider)
}