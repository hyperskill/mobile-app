import SwiftUI
import shared

final class UsersListViewModel: FeatureViewModel<
  UsersListFeatureState,
  UsersListFeatureMessage,
  UsersListFeatureActionViewAction
> {
    func search(query: String) {
        self.onNewMessage(
            UsersListFeatureMessageInit(
                forceUpdate: true,
                usersQuery: UsersQuery(page: 0, pageSize: 20, userName: query)
            )
        )
    }

    func loadNextPage() {
        self.onNewMessage(UsersListFeatureMessageLoadNextPage())
    }
}
