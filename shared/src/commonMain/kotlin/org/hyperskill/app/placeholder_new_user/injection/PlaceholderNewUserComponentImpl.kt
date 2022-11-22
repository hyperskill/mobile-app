package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import ru.nobird.app.presentation.redux.feature.Feature

class PlaceholderNewUserComponentImpl(
    private val appGraph: AppGraph
) : PlaceholderNewUserComponent {
    override val placeholderNewUserFeature: Feature<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action>
        get() = PlaceholderNewUserFeatureBuilder.build(
            appGraph.analyticComponent.analyticInteractor,
            appGraph.authComponent.authInteractor,
            appGraph.networkComponent.authorizationFlow,
            UrlPathProcessor(appGraph.buildMagicLinksDataComponent().magicLinksInteractor)
        )
}