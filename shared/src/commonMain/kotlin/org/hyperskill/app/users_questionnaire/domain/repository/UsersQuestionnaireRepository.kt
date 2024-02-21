package org.hyperskill.app.users_questionnaire.domain.repository

interface UsersQuestionnaireRepository {
    fun getIsUsersQuestionnaireWidgetHidden(): Boolean
    fun setIsUsersQuestionnaireWidgetHidden(isHidden: Boolean)
}