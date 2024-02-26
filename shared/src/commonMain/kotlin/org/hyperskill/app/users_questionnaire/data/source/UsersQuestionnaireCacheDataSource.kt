package org.hyperskill.app.users_questionnaire.data.source

interface UsersQuestionnaireCacheDataSource {
    fun getIsUsersQuestionnaireWidgetHidden(): Boolean
    fun setIsUsersQuestionnaireWidgetHidden(isHidden: Boolean)
}