package org.hyperskill.app.users_questionnaire.data.repository

import org.hyperskill.app.users_questionnaire.data.source.UsersQuestionnaireCacheDataSource
import org.hyperskill.app.users_questionnaire.domain.repository.UsersQuestionnaireRepository

internal class UsersQuestionnaireRepositoryImpl(
    private val usersQuestionnaireCacheDataSource: UsersQuestionnaireCacheDataSource
) : UsersQuestionnaireRepository {
    override fun getIsUsersQuestionnaireWidgetHidden(): Boolean =
        usersQuestionnaireCacheDataSource.getIsUsersQuestionnaireWidgetHidden()

    override fun setIsUsersQuestionnaireWidgetHidden(isHidden: Boolean) {
        usersQuestionnaireCacheDataSource.setIsUsersQuestionnaireWidgetHidden(isHidden)
    }
}