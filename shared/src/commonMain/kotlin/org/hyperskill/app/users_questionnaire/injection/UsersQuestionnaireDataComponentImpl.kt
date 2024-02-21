package org.hyperskill.app.users_questionnaire.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.users_questionnaire.cache.UsersQuestionnaireCacheDataSourceImpl
import org.hyperskill.app.users_questionnaire.data.repository.UsersQuestionnaireRepositoryImpl
import org.hyperskill.app.users_questionnaire.data.source.UsersQuestionnaireCacheDataSource
import org.hyperskill.app.users_questionnaire.domain.repository.UsersQuestionnaireRepository

internal class UsersQuestionnaireDataComponentImpl(
    appGraph: AppGraph
) : UsersQuestionnaireDataComponent {
    private val usersQuestionnaireCacheDataSource: UsersQuestionnaireCacheDataSource =
        UsersQuestionnaireCacheDataSourceImpl(appGraph.commonComponent.settings)

    override val usersQuestionnaireRepository: UsersQuestionnaireRepository
        get() = UsersQuestionnaireRepositoryImpl(usersQuestionnaireCacheDataSource)
}