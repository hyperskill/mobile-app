package org.hyperskill.app.stage_implement.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.projects.domain.interactor.ProjectsInteractor
import org.hyperskill.app.stage_implement.presentation.StageImplementActionDispatcher
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implement.presentation.StageImplementReducer
import org.hyperskill.app.stage_implement.view.mapper.StageImplementViewStateMapper
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StageImplementFeatureBuilder {
    fun build(
        stepRoute: StepRoute,
        projectsInteractor: ProjectsInteractor,
        stagesInteractor: StagesInteractor,
        stepInteractor: StepInteractor
    ): Feature<StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action> {
        check(stepRoute is StepRoute.StageImplement) {
            "Illegal stepRoute=$stepRoute, StepRoute.StageImplement is expected"
        }
        val stageImplementReducer = StageImplementReducer(stepRoute)

        val stageImplementActionDispatcher = StageImplementActionDispatcher(
            ActionDispatcherOptions(),
            projectsInteractor,
            stagesInteractor,
            stepInteractor
        )

        return ReduxFeature(StageImplementFeature.State.Idle, stageImplementReducer)
            .wrapWithActionDispatcher(stageImplementActionDispatcher)
            .transformState(StageImplementViewStateMapper::mapState)
    }
}