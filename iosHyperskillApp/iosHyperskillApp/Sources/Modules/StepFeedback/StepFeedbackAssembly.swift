import Foundation
import shared
import UIKit

final class StepFeedbackAssembly: UIKitAssembly {
    private let stepRoute: StepRoute

    init(stepRoute: StepRoute) {
        self.stepRoute = stepRoute
    }

    func makeModule() -> UIViewController {
        let viewModel = StepFeedbackViewModel(
            stepRoute: stepRoute,
            analyticInteractor: .default
        )

        let alertController = UIAlertController(
            title: Strings.StepFeedback.alertTitle,
            message: nil,
            preferredStyle: .alert
        )
        alertController.addTextField { textField in
            textField.returnKeyType = .send
            textField.enablesReturnKeyAutomatically = true
            textField.placeholder = Strings.StepFeedback.alertHint
            textField.addTarget(
                viewModel,
                action: #selector(StepFeedbackViewModel.handleTextFieldDidChange(_:)),
                for: .editingChanged
            )
        }

        let cancelAction = UIAlertAction(
            title: Strings.StepFeedback.alertCancelButton,
            style: .cancel,
            handler: { _ in
                viewModel.doAlertHidden()
            }
        )
        let sendAction = UIAlertAction(
            title: Strings.StepFeedback.alertSendButton,
            style: .default,
            handler: { _ in
                viewModel.doSend()
                viewModel.doAlertHidden()

                DispatchQueue.main.async {
                    ProgressHUD.showSuccess(status: Strings.StepFeedback.alertSuccessText)
                }
            }
        )
        sendAction.isEnabled = false

        alertController.addAction(sendAction)
        alertController.addAction(cancelAction)

        viewModel.onFeedbackTextDidChange = { [weak sendAction] feedbackText in
            sendAction?.isEnabled = (feedbackText?.isEmpty ?? true) == false
        }

        viewModel.doAlertShown()

        return alertController
    }
}
