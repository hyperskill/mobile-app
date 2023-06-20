package org.hyperskill.app.${package}.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.${package}.presentation.${reducerName}
import org.hyperskill.app.${package}.presentation.${actionDispatcherName}
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Action
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Message
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.State
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ${featureBuilderName} {
    fun build(
        analyticInteractor: AnalyticInteractor
    ): Feature<State, Message, Action> {
        val reducer = ${reducerName}()
        val dispatcher = ${actionDispatcherName}(
            ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )
        return ReduxFeature(State.Idle, reducer)
            .wrapWithActionDispatcher(dispatcher)
    }
}