import shared
import SwiftUI

final class StepQuizCodeBlanksAssembly: Assembly {
    weak var moduleOutput: StepQuizCodeBlanksOutputProtocol?

    private let state: StepQuizCodeBlanksFeatureState

    init(state: StepQuizCodeBlanksFeatureState, moduleOutput: StepQuizCodeBlanksOutputProtocol?) {
        self.state = state
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizCodeBlanksView {
        let viewModel = StepQuizCodeBlanksViewModel()
        viewModel.moduleOutput = moduleOutput

        let viewState = StepQuizCodeBlanksViewStateMapper.shared.map(state: state)

        return StepQuizCodeBlanksView(
            viewStateKs: .init(viewState),
            viewModel: viewModel
        )
    }
}
