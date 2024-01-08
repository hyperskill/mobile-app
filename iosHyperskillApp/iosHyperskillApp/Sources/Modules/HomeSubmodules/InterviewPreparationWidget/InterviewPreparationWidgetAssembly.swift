import shared
import SwiftUI

final class InterviewPreparationWidgetAssembly: Assembly {
    weak var moduleOutput: InterviewPreparationWidgetOutputProtocol?

    private let interviewPreparationWidgetViewStateKs: InterviewPreparationWidgetViewStateKs

    init(
        interviewPreparationWidgetViewStateKs: InterviewPreparationWidgetViewStateKs,
        moduleOutput: InterviewPreparationWidgetOutputProtocol?
    ) {
        self.interviewPreparationWidgetViewStateKs = interviewPreparationWidgetViewStateKs
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> InterviewPreparationWidgetView {
        let viewModel = InterviewPreparationWidgetViewModel()
        viewModel.moduleOutput = moduleOutput

        return InterviewPreparationWidgetView(
            viewStateKs: interviewPreparationWidgetViewStateKs,
            viewModel: viewModel
        )
    }
}
