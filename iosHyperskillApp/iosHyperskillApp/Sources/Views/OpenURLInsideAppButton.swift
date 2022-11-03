import shared
import SwiftUI

struct OpenURLInsideAppButton: View {
    private let text: String

    private let urlType: URLType

    private let webControllerManager: WebControllerManager
    private let webControllerKey: WebControllerManager.WebControllerKey
    private let webControllerType: WebControllerManager.WebControllerType
    private let webControllerBackButtonStyle: WebControllerManager.BackButtonStyle

    private let onTap: (() -> Void)?

    init(
        text: String,
        urlType: URLType,
        webControllerManager: WebControllerManager = .shared,
        webControllerKey: WebControllerManager.WebControllerKey = .externalLink,
        webControllerType: WebControllerManager.WebControllerType,
        webControllerBackButtonStyle: WebControllerManager.BackButtonStyle = .done,
        onTap: (() -> Void)? = nil
    ) {
        self.text = text
        self.urlType = urlType
        self.webControllerManager = webControllerManager
        self.webControllerKey = webControllerKey
        self.webControllerType = webControllerType
        self.webControllerBackButtonStyle = webControllerBackButtonStyle
        self.onTap = onTap
    }

    var body: some View {
        Button(text) {
            onTap?()

            switch urlType {
            case .url(let url):
                webControllerManager.presentWebControllerWithURL(
                    url,
                    withKey: webControllerKey,
                    controllerType: webControllerType,
                    backButtonStyle: webControllerBackButtonStyle
                )
            case .nextURLPath(let nextURLPath):
                webControllerManager.presentWebControllerWithNextURLPath(
                    nextURLPath,
                    withKey: webControllerKey,
                    controllerType: webControllerType,
                    backButtonStyle: webControllerBackButtonStyle
                )
            }
        }
    }

    enum URLType {
        case url(URL)
        case nextURLPath(HyperskillUrlPath)
    }
}

struct URLButton_Previews: PreviewProvider {
    static var previews: some View {
        OpenURLInsideAppButton(
            text: "URL button",
            urlType: .url(URL(string: "https://www.google.com/").require()),
            webControllerType: .inAppSafari
        )
    }
}
