import Foundation
import shared

final class StepFeedbackViewModel {
    private let stepRoute: StepRoute
    private let analyticInteractor: AnalyticInteractor

    private var feedbackText: String? {
        didSet {
            onFeedbackTextDidChange?(feedbackText)
        }
    }
    var onFeedbackTextDidChange: ((String?) -> Void)?

    init(stepRoute: StepRoute, analyticInteractor: AnalyticInteractor) {
        self.stepRoute = stepRoute
        self.analyticInteractor = analyticInteractor
    }

    func doAlertShown() {
        let event = StepFeedbackModalShownHyperskillAnalyticEvent(
            route: stepRoute.analyticRoute,
            stepId: stepRoute.stepId
        )
        analyticInteractor.logEvent(event: event)
    }

    func doAlertHidden() {
        let event = StepFeedbackModalHiddenHyperskillAnalyticEvent(
            route: stepRoute.analyticRoute,
            stepId: stepRoute.stepId
        )
        analyticInteractor.logEvent(event: event)
    }

    func doSend() {
        let event = StepFeedbackModalSendButtonClickedHyperskillAnalyticEvent(
            route: stepRoute.analyticRoute,
            stepId: stepRoute.stepId,
            feedback: feedbackText ?? ""
        )
        analyticInteractor.logEvent(event: event)
    }

    @objc
    func handleTextFieldDidChange(_ textField: UITextField) {
        feedbackText = textField.text
    }
}
