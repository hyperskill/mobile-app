package org.hyperskill.app.users_interview_widget.data.repository

import org.hyperskill.app.users_interview_widget.data.source.UsersInterviewWidgetCacheDataSource
import org.hyperskill.app.users_interview_widget.domain.repository.UsersInterviewWidgetRepository

internal class UsersInterviewWidgetRepositoryImpl(
    private val usersInterviewWidgetCacheDataSource: UsersInterviewWidgetCacheDataSource
) : UsersInterviewWidgetRepository {
    override fun getIsUsersInterviewWidgetHidden(): Boolean =
        usersInterviewWidgetCacheDataSource.getIsUsersInterviewWidgetHidden()

    override fun setIsUsersInterviewWidgetHidden(isHidden: Boolean) {
        usersInterviewWidgetCacheDataSource.setIsUsersInterviewWidgetHidden(isHidden)
    }
}