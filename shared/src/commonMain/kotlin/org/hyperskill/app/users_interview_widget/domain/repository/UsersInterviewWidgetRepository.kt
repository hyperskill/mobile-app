package org.hyperskill.app.users_interview_widget.domain.repository

interface UsersInterviewWidgetRepository {
    fun getIsUsersInterviewWidgetHidden(): Boolean
    fun setIsUsersInterviewWidgetHidden(isHidden: Boolean)
}