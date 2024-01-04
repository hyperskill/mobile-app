import Foundation

final class InterviewPreparationWidgetViewModel {
    weak var moduleOutput: InterviewPreparationWidgetOutputProtocol?

    func doCallToAction() {
        moduleOutput?.handleInterviewPreparationWidgetCallToAction()
    }

    func doRetryContentLoading() {
        moduleOutput?.handleInterviewPreparationWidgetRetryContentLoading()
    }

    func logViewedEvent() {
        moduleOutput?.handleInterviewPreparationWidgetDidAppear()
    }
}
