package org.hyperskill.app.home.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.home.view.mapper.HomeViewStateMapper
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetActionDispatcher
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer
import org.hyperskill.app.interview_preparation.view.mapper.InterviewPreparationWidgetViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetActionDispatcher
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object HomeFeatureBuilder {
    private const val LOG_TAG = "HomeFeature"

    fun build(
        homeInteractor: HomeInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
        stepInteractor: StepInteractor,
        freemiumInteractor: FreemiumInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        dateFormatter: SharedDateFormatter,
        topicRepeatedFlow: TopicRepeatedFlow,
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        challengeWidgetReducer: ChallengeWidgetReducer,
        challengeWidgetActionDispatcher: ChallengeWidgetActionDispatcher,
        challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper,
        interviewPreparationWidgetReducer: InterviewPreparationWidgetReducer,
        interviewPreparationWidgetActionDispatcher: InterviewPreparationWidgetActionDispatcher,
        interviewPreparationWidgetViewStateMapper: InterviewPreparationWidgetViewStateMapper,
        usersQuestionnaireWidgetReducer: UsersQuestionnaireWidgetReducer,
        usersQuestionnaireWidgetActionDispatcher: UsersQuestionnaireWidgetActionDispatcher,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<HomeFeature.ViewState, HomeFeature.Message, HomeFeature.Action> {
        val homeReducer = HomeReducer(
            gamificationToolbarReducer = gamificationToolbarReducer,
            challengeWidgetReducer = challengeWidgetReducer,
            interviewPreparationWidgetReducer = interviewPreparationWidgetReducer,
            usersQuestionnaireWidgetReducer = usersQuestionnaireWidgetReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val homeActionDispatcher = HomeActionDispatcher(
            ActionDispatcherOptions(),
            homeInteractor,
            currentProfileStateRepository,
            topicsRepetitionsInteractor,
            stepInteractor,
            freemiumInteractor,
            analyticInteractor,
            sentryInteractor,
            dateFormatter,
            topicRepeatedFlow
        )
        val homeViewStateMapper = HomeViewStateMapper(
            challengeWidgetViewStateMapper = challengeWidgetViewStateMapper,
            interviewPreparationWidgetViewStateMapper = interviewPreparationWidgetViewStateMapper
        )

        return ReduxFeature(
            HomeFeature.State(
                homeState = HomeFeature.HomeState.Idle,
                toolbarState = GamificationToolbarFeature.State.Idle,
                challengeWidgetState = ChallengeWidgetFeature.State.Idle,
                interviewPreparationWidgetState = InterviewPreparationWidgetFeature.State.Idle,
                usersQuestionnaireWidgetState = UsersQuestionnaireWidgetFeature.State.Idle
            ),
            homeReducer
        )
            .transformState(homeViewStateMapper::map)
            .wrapWithActionDispatcher(homeActionDispatcher)
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<HomeFeature.InternalAction.GamificationToolbarAction>()?.action },
                    transformMessage = HomeFeature.Message::GamificationToolbarMessage
                )
            )
            .wrapWithActionDispatcher(
                challengeWidgetActionDispatcher.transform(
                    transformAction = { it.safeCast<HomeFeature.InternalAction.ChallengeWidgetAction>()?.action },
                    transformMessage = HomeFeature.Message::ChallengeWidgetMessage
                )
            )
            .wrapWithActionDispatcher(
                interviewPreparationWidgetActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<HomeFeature.InternalAction.InterviewPreparationWidgetAction>()?.action
                    },
                    transformMessage = HomeFeature.Message::InterviewPreparationWidgetMessage
                )
            )
            .wrapWithActionDispatcher(
                usersQuestionnaireWidgetActionDispatcher.transform(
                    transformAction = {
                        it.safeCast<HomeFeature.InternalAction.UsersQuestionnaireWidgetAction>()?.action
                    },
                    transformMessage = HomeFeature.Message::UsersQuestionnaireWidgetMessage
                )
            )
    }
}