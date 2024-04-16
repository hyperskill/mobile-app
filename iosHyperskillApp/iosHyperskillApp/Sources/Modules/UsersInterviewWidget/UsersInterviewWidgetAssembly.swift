import shared
import SwiftUI

final class UsersInterviewWidgetAssembly: Assembly {
    weak var moduleOutput: UsersInterviewWidgetOutputProtocol?

    private let stateKs: UsersInterviewWidgetFeatureStateKs

    init(
        stateKs: UsersInterviewWidgetFeatureStateKs,
        moduleOutput: UsersInterviewWidgetOutputProtocol?
    ) {
        self.stateKs = stateKs
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UsersInterviewWidgetView {
        let viewModel = UsersInterviewWidgetViewModel()
        viewModel.moduleOutput = moduleOutput

        return UsersInterviewWidgetView(
            stateKs: stateKs,
            viewModel: viewModel
        )
    }
}
