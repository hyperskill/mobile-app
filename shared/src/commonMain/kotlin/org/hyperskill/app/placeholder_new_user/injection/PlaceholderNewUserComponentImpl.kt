package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import ru.nobird.app.presentation.redux.feature.Feature

class PlaceholderNewUserComponentImpl(
    private val analyticInteractor: AnalyticInteractor
) : PlaceholderNewUserComponent {
    override val placeholderNewUserFeature: Feature<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action>
        get() = PlaceholderNewUserFeatureBuilder.build(analyticInteractor)
}