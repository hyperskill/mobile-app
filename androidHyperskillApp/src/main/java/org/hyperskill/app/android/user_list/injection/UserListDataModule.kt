package org.hyperskill.app.android.user_list.injection

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import org.hyperskill.app.android.network.injection.StubHttpClient
import org.hyperskill.app.user_list.data.repository.UserListRepositoryImpl
import org.hyperskill.app.user_list.data.source.UserListRemoteDataSource
import org.hyperskill.app.user_list.domain.interactor.UserListInteractor
import org.hyperskill.app.user_list.domain.repository.UserListRepository
import org.hyperskill.app.user_list.remote.UserListRemoteDataSourceImpl

@Module
object UserListDataModule {
    @Provides
    fun provideUsersListRemoteDataSource(
        @StubHttpClient
        httpClient: HttpClient
    ): UserListRemoteDataSource =
        UserListRemoteDataSourceImpl(httpClient)

    @Provides
    fun provideUsersListRepository(userListRemoteDataSource: UserListRemoteDataSource): UserListRepository =
        UserListRepositoryImpl(userListRemoteDataSource)

    @Provides
    fun provideUsersListInteractor(userListRepository: UserListRepository): UserListInteractor =
        UserListInteractor(userListRepository)
}