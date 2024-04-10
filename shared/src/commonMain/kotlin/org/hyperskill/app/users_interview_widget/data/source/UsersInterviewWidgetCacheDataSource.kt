package org.hyperskill.app.users_interview_widget.data.source

interface UsersInterviewWidgetCacheDataSource {
    fun getIsUsersInterviewWidgetHidden(): Boolean
    fun setIsUsersInterviewWidgetHidden(isHidden: Boolean)
}