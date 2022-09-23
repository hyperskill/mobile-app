import MessageUI
import shared
import SVProgressHUD
import UIKit

final class SendEmailFeedbackController: NSObject {
    private weak var presentationController: UIViewController?

    func sendFeedback(feedbackEmailData: FeedbackEmailData, presentationController: UIViewController) {
        self.presentationController = presentationController

        guard MFMailComposeViewController.canSendMail() else {
            return presentNotAbleSendMailAlert()
        }

        let mailComposeViewController = MFMailComposeViewController()
        mailComposeViewController.mailComposeDelegate = self

        mailComposeViewController.setToRecipients([feedbackEmailData.mailTo])
        mailComposeViewController.setSubject(feedbackEmailData.subject)
        mailComposeViewController.setMessageBody(feedbackEmailData.body, isHTML: false)

        self.presentationController?.present(mailComposeViewController, animated: true)
    }

    private func presentNotAbleSendMailAlert() {
        let alert = UIAlertController(
            title: NSLocalizedString("SendEmailFeedbackControllerNotAbleSendEmailAlertTitle", comment: ""),
            message: NSLocalizedString("SendEmailFeedbackControllerNotAbleSendEmailAlertMessage", comment: ""),
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .default))
        presentationController?.present(alert, animated: true)
    }

    private func presentSendMailErrorAlert() {
        let alert = UIAlertController(
            title: NSLocalizedString("Error", comment: ""),
            message: NSLocalizedString("SendEmailFeedbackControllerSendMailErrorAlertMessage", comment: ""),
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .default))
        presentationController?.present(alert, animated: true)
    }
}

extension SendEmailFeedbackController: MFMailComposeViewControllerDelegate {
    func mailComposeController(
        _ controller: MFMailComposeViewController,
        didFinishWith result: MFMailComposeResult,
        error: Error?
    ) {
        if result == .failed || error != nil {
            presentSendMailErrorAlert()
        } else {
            presentationController?.dismiss(
                animated: true,
                completion: {
                    if result == .sent {
                        SVProgressHUD.showSuccess(
                            withStatus: NSLocalizedString(
                                "SendEmailFeedbackControllerSendMailSuccessMessage",
                                comment: ""
                            )
                        )
                    }
                }
            )
        }
    }
}
