package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.presentation.ProfileFeature
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileComponentImpl(private val appGraph: AppGraph) : ProfileComponent {
    override val profileFeature: Feature<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action>
        get() = ProfileFeatureBuilder.build(
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.streakFlowDataComponent.streakFlow,
            appGraph.buildProductsDataComponent().productsInteractor,
            appGraph.buildItemsDataComponent().itemsInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor
        )
}