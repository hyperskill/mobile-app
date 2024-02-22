package org.hyperskill.app.users_questionnaire.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.users_questionnaire.data.source.UsersQuestionnaireCacheDataSource

internal class UsersQuestionnaireCacheDataSourceImpl(
    private val settings: Settings
) : UsersQuestionnaireCacheDataSource {
    override fun getIsUsersQuestionnaireWidgetHidden(): Boolean =
        settings.getBoolean(UsersQuestionnaireCacheKeyValues.USERS_QUESTIONNAIRE_WIDGET_HIDDEN, false)

    override fun setIsUsersQuestionnaireWidgetHidden(isHidden: Boolean) {
        settings.putBoolean(UsersQuestionnaireCacheKeyValues.USERS_QUESTIONNAIRE_WIDGET_HIDDEN, isHidden)
    }
}