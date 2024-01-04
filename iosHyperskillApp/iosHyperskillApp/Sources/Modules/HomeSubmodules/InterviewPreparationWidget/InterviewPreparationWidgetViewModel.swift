import Foundation

final class InterviewPreparationWidgetViewModel {
    weak var moduleOutput: InterviewPreparationWidgetOutputProtocol?

    func doRetryContentLoading() {
        moduleOutput?.handleInterviewPreparationWidgetRetryContentLoading()
    }
}
