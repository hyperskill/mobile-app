import SwiftUI
import shared

struct UsersListView: View {
    @ObservedObject var viewModel: UsersListViewModel

    @State private var query = "eadm"

    init(viewModel: UsersListViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        let state = self.viewModel.state

        VStack {
            HStack(alignment: .top) {
                TextField("Query", text: $query)

                Button(action: self.searchButtonPressed) {
                    Text("Search")
                }
            }
            .padding()

            switch state {
            case is UsersListFeatureStateIdle:
                Text("Empty")
            case is UsersListFeatureStateLoading:
                ProgressView()
            case let data as UsersListFeatureStateData:
                ScrollView {
                    LazyVStack {
                        ForEach(data.users, id: \.id) { user in
                            UsersListRowView(user: user)
                                .onAppear {
                                    self.loadMoreIfNeeded(user: user)
                                }
                        }

                        if data.isLoading {
                            ProgressView()
                        }
                    }
                }
            case is UsersListFeatureStateNetworkError:
                Text("Network error")
            default:
                Text("Unkwown state")
            }

            Spacer()
        }
        .onAppear(perform: self.viewModel.startListening)
        .onDisappear(perform: self.viewModel.stopListening)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: UsersListFeatureActionViewAction) {
        print("UsersListView :: \(#function) viewAction = \(viewAction)")
    }

    private func searchButtonPressed() {
        self.viewModel.search(query: self.query)
    }

    private func loadMoreIfNeeded(user: User) {
        guard let data = self.viewModel.state as? UsersListFeatureStateData,
              data.users.last == user else {
            return
        }

        self.viewModel.loadNextPage()
    }
}

// MARK: - UsersListView_Previews: PreviewProvider -

struct UsersListView_Previews: PreviewProvider {
    static var previews: some View {
        UsersListAssembly().makeModule()
    }
}
