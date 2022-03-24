import Combine
import SwiftUI
import shared

final class UsersListViewModel: ObservableObject {
    private let usersListFeature: Presentation_reduxFeature

    @ObservedObject var stateStore: FeatureStateStore<UsersListFeatureState>
    private var stateStoreAnyCancellable: AnyCancellable?

    var state: UsersListFeatureState {
        self.stateStore.state
    }

    init(usersListFeature: Presentation_reduxFeature) {
        self.usersListFeature = usersListFeature
        self.stateStore = FeatureStateStore(
            feature: usersListFeature,
            initialState: UsersListFeatureStateIdle()
        )
    }

    // MARK: Public API

    func startListening() {
        self.stopListening()

        self.stateStore.addStateListener()
        self.stateStoreAnyCancellable = self.stateStore.objectWillChange.sink { [weak self] _ in
            self?.objectWillChange.send()
        }
    }

    func stopListening() {
        self.stateStore.cancelStateListener()

        self.stateStoreAnyCancellable?.cancel()
        self.stateStoreAnyCancellable = nil
    }

    func search(query: String) {
        self.usersListFeature.onNewMessage(
            message_: UsersListFeatureMessageInit(
                forceUpdate: true,
                usersQuery: UsersQuery(page: 0, pageSize: 20, userName: query)
            )
        )
    }

    func loadNextPage() {
        self.usersListFeature.onNewMessage(message_: UsersListFeatureMessageLoadNextPage())
    }
}
