import shared
import SwiftUI

struct AppView: View {
    @ObservedObject private var viewModel: AppViewModel

    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        UsersListAssembly().makeModule()
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AppFeatureActionViewAction) {
        print("AppView :: \(#function) viewAction = \(viewAction)")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AppAssembly().makeModule()
    }
}
