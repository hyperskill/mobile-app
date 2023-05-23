package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.view.mapper.PlaceholderNewUserViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

class PlaceholderNewUserComponentImpl(
    private val appGraph: AppGraph
) : PlaceholderNewUserComponent {
    override val placeholderNewUserFeature: Feature<
        PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action>
        get() = PlaceholderNewUserFeatureBuilder.build(
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildFreemiumDataComponent().freemiumInteractor
        )

    override val placeHolderNewUserViewDataMapper: PlaceholderNewUserViewDataMapper
        get() = PlaceholderNewUserViewDataMapper(appGraph.commonComponent.dateFormatter)
}