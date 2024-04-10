package org.hyperskill.app.users_interview_widget.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.users_interview_widget.data.source.UsersInterviewWidgetCacheDataSource

internal class UsersInterviewWidgetCacheDataSourceImpl(
    private val settings: Settings
) : UsersInterviewWidgetCacheDataSource {
    override fun getIsUsersInterviewWidgetHidden(): Boolean =
        settings.getBoolean(UsersInterviewWidgetCacheKeyValues.USERS_INTERVIEW_WIDGET_HIDDEN, false)

    override fun setIsUsersInterviewWidgetHidden(isHidden: Boolean) {
        settings.putBoolean(UsersInterviewWidgetCacheKeyValues.USERS_INTERVIEW_WIDGET_HIDDEN, isHidden)
    }
}