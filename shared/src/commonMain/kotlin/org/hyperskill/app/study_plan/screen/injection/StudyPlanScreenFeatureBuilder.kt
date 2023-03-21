package org.hyperskill.app.study_plan.screen.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenActionDispatcher
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenReducer
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewStateMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewStateMapper
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StudyPlanScreenFeatureBuilder {
    fun build(
        analyticInteractor: AnalyticInteractor,
        toolbarReducer: GamificationToolbarReducer,
        toolbarActionDispatcher: GamificationToolbarActionDispatcher,
        studyPlanWidgetReducer: StudyPlanWidgetReducer,
        studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher,
        studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper
    ): Feature<StudyPlanScreenViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action> {
        val studyPlanScreenReducer = StudyPlanScreenReducer(toolbarReducer, studyPlanWidgetReducer)
        val studyPlanScreenActionDispatcher = StudyPlanScreenActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )
        val studyPlanScreenViewStateMapper = StudyPlanScreenViewStateMapper(studyPlanWidgetViewStateMapper)
        return ReduxFeature(
            StudyPlanScreenFeature.State(
                toolbarState = GamificationToolbarFeature.State.Idle,
                studyPlanWidgetState = StudyPlanWidgetFeature.State()
            ),
            reducer = studyPlanScreenReducer
        )
            .transformState(studyPlanScreenViewStateMapper::map)
            .wrapWithActionDispatcher(studyPlanScreenActionDispatcher)
            .wrapWithActionDispatcher(
                toolbarActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<StudyPlanScreenFeature.InternalAction.GamificationToolbarAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::GamificationToolbarMessage
                )
            )
            .wrapWithActionDispatcher(
                studyPlanWidgetDispatcher.transform(
                    transformAction = {
                        it.safeCast<StudyPlanScreenFeature.InternalAction.StudyPlanWidgetAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::StudyPlanWidgetMessage
                )
            )
    }
}