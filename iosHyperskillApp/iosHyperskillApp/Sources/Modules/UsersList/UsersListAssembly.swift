import shared
import SwiftUI

final class UsersListAssembly: Assembly {
    func makeModule() -> UsersListView {
        let userListInteractor = UserListInteractor(
            userListRepository: UserListRepositoryImpl(
                userListRemoteDataSource: UserListRemoteDataSourceImpl(
                    httpClient: NetworkModule.shared.provideClient(json: NetworkModule.shared.provideJson())
                )
            )
        )
        let usersListFeature = UsersListFeatureBuilder.shared.build(userListInteractor: userListInteractor)

        let usersListViewModel = UsersListViewModel(feature: usersListFeature)

        return UsersListView(viewModel: usersListViewModel)
    }
}
