package org.hyperskill.app.users_interview_widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.users_interview_widget.cache.UsersInterviewWidgetCacheDataSourceImpl
import org.hyperskill.app.users_interview_widget.data.repository.UsersInterviewWidgetRepositoryImpl
import org.hyperskill.app.users_interview_widget.data.source.UsersInterviewWidgetCacheDataSource
import org.hyperskill.app.users_interview_widget.domain.repository.UsersInterviewWidgetRepository
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetActionDispatcher
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetReducer

internal class UsersInterviewWidgetComponentImpl(
    private val appGraph: AppGraph
) : UsersInterviewWidgetComponent {
    private val usersInterviewWidgetCacheDataSource: UsersInterviewWidgetCacheDataSource =
        UsersInterviewWidgetCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val usersInterviewWidgetRepository: UsersInterviewWidgetRepository =
        UsersInterviewWidgetRepositoryImpl(usersInterviewWidgetCacheDataSource)

    override val usersInterviewWidgetReducer: UsersInterviewWidgetReducer
        get() = UsersInterviewWidgetReducer()

    override val usersInterviewWidgetActionDispatcher: UsersInterviewWidgetActionDispatcher
        get() = UsersInterviewWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            usersInterviewWidgetRepository = usersInterviewWidgetRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}