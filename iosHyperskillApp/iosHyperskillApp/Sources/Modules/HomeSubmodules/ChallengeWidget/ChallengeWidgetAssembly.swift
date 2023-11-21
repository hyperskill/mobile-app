import shared
import SwiftUI

final class ChallengeWidgetAssembly: Assembly {
    weak var moduleOutput: ChallengeWidgetOutputProtocol?

    private let challengeWidgetViewStateKs: ChallengeWidgetViewStateKs

    init(challengeWidgetViewStateKs: ChallengeWidgetViewStateKs, moduleOutput: ChallengeWidgetOutputProtocol?) {
        self.moduleOutput = moduleOutput
        self.challengeWidgetViewStateKs = challengeWidgetViewStateKs
    }

    func makeModule() -> ChallengeWidgetView {
        let viewModel = ChallengeWidgetViewModel()
        viewModel.moduleOutput = moduleOutput

        return ChallengeWidgetView(
            viewStateKs: challengeWidgetViewStateKs,
            viewModel: viewModel
        )
    }
}
