package org.hyperskill.app.study_plan.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenActionDispatcher
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenReducer
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewStateMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetReducer
import org.hyperskill.app.study_plan.widget.view.mapper.StudyPlanWidgetViewStateMapper
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object StudyPlanScreenFeatureBuilder {
    private const val LOG_TAG = "StudyPlanScreenFeature"
    fun build(
        analyticInteractor: AnalyticInteractor,
        toolbarReducer: GamificationToolbarReducer,
        toolbarActionDispatcher: GamificationToolbarActionDispatcher,
        problemsLimitReducer: ProblemsLimitReducer,
        problemsLimitActionDispatcher: ProblemsLimitActionDispatcher,
        studyPlanWidgetReducer: StudyPlanWidgetReducer,
        studyPlanWidgetDispatcher: StudyPlanWidgetActionDispatcher,
        problemsLimitViewStateMapper: ProblemsLimitViewStateMapper,
        studyPlanWidgetViewStateMapper: StudyPlanWidgetViewStateMapper,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action> {
        val studyPlanScreenReducer = StudyPlanScreenReducer(
            toolbarReducer = toolbarReducer,
            problemsLimitReducer = problemsLimitReducer,
            studyPlanWidgetReducer = studyPlanWidgetReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val studyPlanScreenActionDispatcher = StudyPlanScreenActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )
        val studyPlanScreenViewStateMapper =
            StudyPlanScreenViewStateMapper(
                problemsLimitViewStateMapper = problemsLimitViewStateMapper,
                studyPlanWidgetViewStateMapper = studyPlanWidgetViewStateMapper,
                resourceProvider = resourceProvider
            )
        return ReduxFeature(
            StudyPlanScreenFeature.State(
                toolbarState = GamificationToolbarFeature.State.Idle,
                problemsLimitState = ProblemsLimitFeature.State.Idle,
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
                problemsLimitActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<StudyPlanScreenFeature.InternalAction.ProblemsLimitAction>()?.action
                    },
                    transformMessage = StudyPlanScreenFeature.Message::ProblemsLimitMessage
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