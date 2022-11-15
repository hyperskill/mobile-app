package org.hyperskill.app.user_storage.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.user_storage.cache.UserStorageCacheDataSourceImpl
import org.hyperskill.app.user_storage.data.repository.UserStorageRepositoryImpl
import org.hyperskill.app.user_storage.data.source.UserStorageCacheDataSource
import org.hyperskill.app.user_storage.data.source.UserStorageRemoteDataSource
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.repository.UserStorageRepository
import org.hyperskill.app.user_storage.remote.UserStorageRemoteDataSourceImpl

class UserStorageComponentImpl(appGraph: AppGraph) : UserStorageComponent {
    private val userStorageRemoteDataSource: UserStorageRemoteDataSource =
        UserStorageRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val userStorageCacheDataSource: UserStorageCacheDataSource =
        UserStorageCacheDataSourceImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )

    override val userStorageRepository: UserStorageRepository =
        UserStorageRepositoryImpl(userStorageRemoteDataSource, userStorageCacheDataSource)

    override val userStorageInteractor: UserStorageInteractor
        get() = UserStorageInteractor(userStorageRepository)
}