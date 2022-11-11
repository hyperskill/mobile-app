import Foundation

protocol WebOAuthServiceProtocol: AnyObject {
    func signIn(registerURL: URL) async throws -> String
}

enum WebOAuthError: Error {
    case canceled
    case accessDenied
    case noPresentingViewController
}

@MainActor
final class WebOAuthService: WebOAuthServiceProtocol {
    static let shared = WebOAuthService()

    private let sourcelessRouter: SourcelessRouter

    private var continuation: CheckedContinuation<String, Error>?

    private init() {
        self.sourcelessRouter = SourcelessRouter()
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(didReceiveAuthCode(_:)),
            name: .socialAuthDidReceiveOAuthCode,
            object: nil
        )
    }

    func signIn(registerURL: URL) async throws -> String {
        guard let currentPresentedViewController = self.sourcelessRouter.currentPresentedViewController() else {
            throw WebOAuthError.noPresentingViewController
        }

        WebControllerManager.shared.presentWebControllerWithURL(
            registerURL,
            inController: currentPresentedViewController,
            withKey: .socialAuth,
            controllerType: .inAppCustom(allowsOpenInSafari: false),
            backButtonStyle: .close
        )
        WebControllerManager.shared.onDismissCustomWebController = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.continuation?.resume(throwing: WebOAuthError.canceled)
            strongSelf.continuation = nil
        }

        return try await withCheckedThrowingContinuation { self.continuation = $0 }
    }

    @objc
    private func didReceiveAuthCode(_ notification: NSNotification) {
        #if DEBUG
        print("WebOAuthService :: didReceiveAuthCode, notification = \(notification)")
        #endif

        WebControllerManager.shared.dismissWebControllerWithKey(.socialAuth)
        WebControllerManager.shared.onDismissCustomWebController = nil

        if let code = notification.userInfo?[WebOAuthURLParser.Key.code.rawValue] as? String {
            self.continuation?.resume(returning: code)
        } else {
            self.continuation?.resume(throwing: WebOAuthError.accessDenied)
        }

        self.continuation = nil
    }
}

extension NSNotification.Name {
    static let socialAuthDidReceiveOAuthCode = NSNotification.Name("SocialAuthDidReceiveOAuthCode")
}
