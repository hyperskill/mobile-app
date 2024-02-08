import shared
import UIKit

protocol ApplicationShortcutsServiceProtocol: AnyObject {
    func handleShortcutItem(_ shortcutItem: UIApplicationShortcutItem) -> Bool
    func handleLaunchOptions(_ launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool
}

final class ApplicationShortcutsService: ApplicationShortcutsServiceProtocol {
    private lazy var applicationShortcutsInteractor: ApplicationShortcutsInteractor =
        AppGraphBridge.sharedAppGraph.buildApplicationShortcutsDataComponent().applicationShortcutsInteractor

    private lazy var analyticInteractor = AnalyticInteractor.default

    private var sendEmailFeedbackController: SendEmailFeedbackController?

    // MARK: Protocol Conforming

    func handleLaunchOptions(_ launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        if let shortcutItem = launchOptions?[.shortcutItem] as? UIApplicationShortcutItem {
            _ = handleShortcutItem(shortcutItem)
            return true
        }
        return false
    }

    func handleShortcutItem(_ shortcutItem: UIApplicationShortcutItem) -> Bool {
        let shortcutType = shortcutItem.type

        analyticInteractor.logEvent(
            event: ApplicationShortcutItemClickedHyperskillAnalyticEvent(shortcutItemIdentifier: shortcutType)
        )

        guard let shortcutIdentifier = ApplicationShortcutIdentifier(fullIdentifier: shortcutType) else {
            #if DEBUG
            print("ApplicationShortcutsService: Did receive unknown shortcut identifier: \(shortcutType)")
            #endif
            return false
        }

        DispatchQueue.main.async {
            self.performAction(for: shortcutIdentifier)
        }

        return true
    }

    // MARK: Private API

    private func performAction(for shortcutIdentifier: ApplicationShortcutIdentifier) {
        switch shortcutIdentifier {
        case .sendFeedback:
            performSendFeedback()
        }
    }

    private func performSendFeedback() {
        applicationShortcutsInteractor.getSendFeedbackEmailData { [weak self] feedbackEmailData, error in
            if let error {
                #if DEBUG
                print("ApplicationShortcutsService: SendFeedback, failed get email data: \(error)")
                #endif
                return
            }

            guard let feedbackEmailData else {
                #if DEBUG
                print("ApplicationShortcutsService: SendFeedback, no email data")
                #endif
                return
            }

            assert(Thread.current.isMainThread)

            guard let currentPresentedViewController = SourcelessRouter().currentPresentedViewController() else {
                #if DEBUG
                print("ApplicationShortcutsService: SendFeedback, no current presented view controller")
                #endif
                return
            }

            let sendEmailFeedbackController = SendEmailFeedbackController()
            sendEmailFeedbackController.onDidFinish = { [weak self] in
                self?.sendEmailFeedbackController = nil
            }
            self?.sendEmailFeedbackController = sendEmailFeedbackController

            sendEmailFeedbackController.sendFeedback(
                feedbackEmailData: feedbackEmailData,
                presentationController: currentPresentedViewController
            )
        }
    }
}
