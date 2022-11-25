import SafariServices
import shared
import SVProgressHUD
import UIKit
import WebKit

final class WebControllerManager: NSObject {
    static let shared = WebControllerManager()

    private var currentWebControllerKey: WebControllerKey?
    private var currentWebController: UIViewController? {
        willSet {
            guard newValue != nil else {
                return
            }

            if let currentWebController = self.currentWebController {
                currentWebController.dismiss(animated: false, completion: nil)
                print("Web controllers conflict! Dismissed the underlying one.")
            }
        }
    }

    var onDismissCustomWebController: (() -> Void)?

    // MARK: Public API

    func dismissWebControllerWithKey(
        _ key: WebControllerKey,
        animated: Bool = true,
        completion: (() -> Void)? = nil,
        error: ((String) -> Void)? = nil
    ) {
        if let currentWebController = self.currentWebController,
           let currentWebControllerKey = self.currentWebControllerKey {
            if currentWebControllerKey == key {
                currentWebController.dismiss(animated: animated, completion: completion)
                self.currentWebController = nil
                self.currentWebControllerKey = nil
                return
            }
        }

        print(self.currentWebController ?? "")
        error?("Could not dismiss web controller with key \(key)")
    }

    func presentWebControllerWithURL(
        _ url: URL,
        inController controller: UIViewController? = nil,
        withKey key: WebControllerKey = .externalLink,
        controllerType: WebControllerType = .safari,
        backButtonStyle: BackButtonStyle = .done,
        animated: Bool = true
    ) {
        guard let controller = controller ?? SourcelessRouter().currentPresentedViewController() else {
            return
        }

        func present(url: URL) {
            switch controllerType {
            case .safari:
                UIApplication.shared.open(url)
            case .inAppSafari:
                let safariViewController = SFSafariViewController(url: url)
                safariViewController.modalPresentationStyle = .fullScreen

                self.currentWebControllerKey = key
                self.currentWebController = safariViewController

                controller.present(safariViewController, animated: true)
            case .inAppCustom(let allowsOpenInSafari):
                self.currentWebControllerKey = key
                self.presentCustomWebController(
                    url,
                    inController: controller,
                    allowsSafari: allowsOpenInSafari,
                    backButtonStyle: backButtonStyle,
                    animated: animated
                )
            }
        }

        guard ["http", "https"].contains(url.scheme?.lowercased() ?? "") else {
            return UIApplication.shared.open(url)
        }

        if key == .socialAuth {
            return present(url: url)
        }

        let queryParameters = ["from_mobile_app": "true"]

        guard let url = url.appendingQueryParameters(queryParameters) else {
            return
        }

        present(url: url)
    }

    func presentWebControllerWithURLString(
        _ urlString: String,
        inController controller: UIViewController? = nil,
        withKey key: WebControllerKey = .externalLink,
        controllerType: WebControllerType = .safari,
        backButtonStyle: BackButtonStyle = .done,
        animated: Bool = true
    ) {
        let urlOrNil: URL? = {
            if let url = URL(string: urlString) {
                return url
            } else if let urlEncodedString = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed),
                      let url = URL(string: urlEncodedString) {
                return url
            }
            return nil
        }()

        guard let url = urlOrNil else {
            return print("Invalid url = \(urlString)")
        }

        self.presentWebControllerWithURL(
            url,
            inController: controller,
            withKey: key,
            controllerType: controllerType,
            backButtonStyle: backButtonStyle,
            animated: animated
        )
    }

    // MARK: Private API

    private func presentCustomWebController(
        _ url: URL,
        inController presentingViewController: UIViewController,
        allowsSafari: Bool = true,
        backButtonStyle: BackButtonStyle,
        animated: Bool = true
    ) {
        if self.currentWebControllerKey == .socialAuth {
            WebCacheCleaner.clean()
        }

        let urlRequest = URLRequest(
            url: url,
            cachePolicy: self.currentWebControllerKey == .socialAuth
                ? .reloadIgnoringLocalCacheData
                : .useProtocolCachePolicy
        )

        let controller = WebViewController(urlRequest: urlRequest)
        controller.allowsToOpenInSafari = allowsSafari
        controller.backButtonStyle = backButtonStyle
        controller.webView.navigationDelegate = self
        controller.webView.uiDelegate = self
        controller.onDismiss = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.onDismissCustomWebController?()

            strongSelf.currentWebController?.dismiss(animated: true, completion: nil)
            strongSelf.currentWebController = nil
            strongSelf.currentWebControllerKey = nil
        }

        let navigationController = UINavigationController(rootViewController: controller)
        navigationController.modalPresentationStyle = .fullScreen

        self.currentWebController = navigationController

        presentingViewController.present(navigationController, animated: animated, completion: nil)
    }

    // MARK: Inner Types

    enum WebControllerKey {
        case socialAuth
        case externalLink
    }

    enum WebControllerType {
        case safari
        case inAppSafari
        case inAppCustom(allowsOpenInSafari: Bool = true)
    }

    enum BackButtonStyle {
        case done
        case close

        var barButtonItem: UIBarButtonItem {
            switch self {
            case .done:
                return UIBarButtonItem(barButtonSystemItem: .done, target: nil, action: nil)
            case .close:
                return UIBarButtonItem(barButtonSystemItem: .close, target: nil, action: nil)
            }
        }
    }
}

// MARK: - WebControllerManager: WKNavigationDelegate -

extension WebControllerManager: WKNavigationDelegate {
    func webView(
        _ webView: WKWebView,
        decidePolicyFor navigationAction: WKNavigationAction,
        decisionHandler: @escaping (WKNavigationActionPolicy) -> Void
    ) {
        defer {
            decisionHandler(.allow)
        }

        guard navigationAction.targetFrame != nil,
              let requestURL = navigationAction.request.url else {
            return
        }

        if WebOAuthURLParser.isRedirectURI(requestURL),
           let code = WebOAuthURLParser.getCodeQueryValue(requestURL) {
            NotificationCenter.default.post(
                name: .socialAuthDidReceiveOAuthCode,
                object: self,
                userInfo: [WebOAuthURLParser.Key.code.rawValue: code]
            )
        }
    }
}

// MARK: - WebControllerManager: WKUIDelegate -

extension WebControllerManager: WKUIDelegate {
    func webView(
        _ webView: WKWebView,
        runJavaScriptAlertPanelWithMessage message: String,
        initiatedByFrame frame: WKFrameInfo,
        completionHandler: @escaping () -> Void
    ) {
        if let currentWebController = self.currentWebController {
            WKWebViewPanelManager.presentAlert(
                on: currentWebController,
                title: NSLocalizedString("Alert", comment: ""),
                message: message,
                handler: completionHandler
            )
        }
    }

    func webView(
        _ webView: WKWebView,
        runJavaScriptConfirmPanelWithMessage message: String,
        initiatedByFrame frame: WKFrameInfo,
        completionHandler: @escaping (Bool) -> Void
    ) {
        if let currentWebController = self.currentWebController {
            WKWebViewPanelManager.presentConfirm(
                on: currentWebController,
                title: NSLocalizedString("Confirm", comment: ""),
                message: message,
                handler: completionHandler
            )
        }
    }

    func webView(
        _ webView: WKWebView,
        runJavaScriptTextInputPanelWithPrompt prompt: String,
        defaultText: String?,
        initiatedByFrame frame: WKFrameInfo,
        completionHandler: @escaping (String?) -> Void
    ) {
        if let currentWebController = self.currentWebController {
            WKWebViewPanelManager.presentPrompt(
                on: currentWebController,
                title: NSLocalizedString("Prompt", comment: ""),
                message: prompt,
                defaultText: defaultText,
                handler: completionHandler
            )
        }
    }
}
