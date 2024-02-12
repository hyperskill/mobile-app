package org.hyperskill.app.home.injection

import org.hyperskill.app.challenges.widget.injection.ChallengeWidgetComponent
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.interview_preparation.injection.InterviewPreparationWidgetComponent
import org.hyperskill.app.users_questionnaire.widget.injection.UsersQuestionnaireWidgetComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.HOME)

    private val challengeWidgetComponent: ChallengeWidgetComponent =
        appGraph.buildChallengeWidgetComponent()

    private val interviewPreparationWidgetComponent: InterviewPreparationWidgetComponent =
        appGraph.buildInterviewPreparationWidgetComponent()

    private val usersQuestionnaireWidgetComponent: UsersQuestionnaireWidgetComponent =
        appGraph.buildUsersQuestionnaireWidgetComponent()

    override val homeFeature: Feature<HomeFeature.ViewState, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            homeInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.buildTopicsRepetitionsDataComponent().topicsRepetitionsInteractor,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.dateFormatter,
            appGraph.topicsRepetitionsFlowDataComponent.topicRepeatedFlow,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            challengeWidgetComponent.challengeWidgetReducer,
            challengeWidgetComponent.challengeWidgetActionDispatcher,
            challengeWidgetComponent.challengeWidgetViewStateMapper,
            interviewPreparationWidgetComponent.interviewPreparationWidgetReducer,
            interviewPreparationWidgetComponent.interviewPreparationWidgetActionDispatcher,
            interviewPreparationWidgetComponent.interviewPreparationWidgetViewStateMapper,
            usersQuestionnaireWidgetComponent.usersQuestionnaireWidgetReducer,
            usersQuestionnaireWidgetComponent.usersQuestionnaireWidgetActionDispatcher,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}