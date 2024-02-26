package org.hyperskill.app.users_questionnaire.widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetActionDispatcher
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetReducer

internal class UsersQuestionnaireWidgetComponentImpl(
    private val appGraph: AppGraph
) : UsersQuestionnaireWidgetComponent {
    override val usersQuestionnaireWidgetReducer: UsersQuestionnaireWidgetReducer
        get() = UsersQuestionnaireWidgetReducer()

    override val usersQuestionnaireWidgetActionDispatcher: UsersQuestionnaireWidgetActionDispatcher
        get() = UsersQuestionnaireWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            usersQuestionnaireRepository = appGraph.buildUsersQuestionnaireDataComponent().usersQuestionnaireRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}