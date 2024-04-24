import Foundation
import shared

@MainActor
enum StepQuizHintsViewActionHandler {
    static func handle(viewAction: StepQuizHintsFeatureActionViewAction) {
        switch StepQuizHintsFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.Common.connectionError)
        }
    }
}
