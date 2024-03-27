import Foundation
import shared
import UIKit

final class StepTheoryFeedbackModalAssembly: UIKitAssembly {
    private let stepRoute: StepRoute

    init(stepRoute: StepRoute) {
        self.stepRoute = stepRoute
    }

    func makeModule() -> UIViewController {
        let viewModel = StepTheoryFeedbackViewModel(
            stepRoute: stepRoute,
            analyticInteractor: .default
        )

        let alertController = UIAlertController(
            title: Strings.Step.TheoryFeedback.alertTitle,
            message: nil,
            preferredStyle: .alert
        )
        alertController.addTextField { textField in
            textField.returnKeyType = .send
            textField.enablesReturnKeyAutomatically = true
            textField.placeholder = Strings.Step.TheoryFeedback.alertHint
            textField.addTarget(
                viewModel,
                action: #selector(StepTheoryFeedbackViewModel.handleTextFieldDidChange(_:)),
                for: .editingChanged
            )
        }

        let cancelAction = UIAlertAction(
            title: Strings.Step.TheoryFeedback.alertCancelButton,
            style: .cancel,
            handler: { _ in
                viewModel.doAlertHidden()
            }
        )
        let sendAction = UIAlertAction(
            title: Strings.Step.TheoryFeedback.alertSendButton,
            style: .default,
            handler: { _ in
                viewModel.doSend()
                viewModel.doAlertHidden()

                DispatchQueue.main.async {
                    ProgressHUD.showSuccess(status: Strings.Step.TheoryFeedback.alertSuccessText)
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
