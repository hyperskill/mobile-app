import shared
import SwiftUI

final class UsersQuestionnaireWidgetAssembly: Assembly {
    weak var moduleOutput: UsersQuestionnaireWidgetOutputProtocol?

    private let stateKs: UsersQuestionnaireWidgetFeatureStateKs

    init(
        stateKs: UsersQuestionnaireWidgetFeatureStateKs,
        moduleOutput: UsersQuestionnaireWidgetOutputProtocol?
    ) {
        self.stateKs = stateKs
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UsersQuestionnaireWidgetView {
        let viewModel = UsersQuestionnaireWidgetViewModel()
        viewModel.moduleOutput = moduleOutput

        return UsersQuestionnaireWidgetView(
            stateKs: stateKs,
            viewModel: viewModel
        )
    }
}
