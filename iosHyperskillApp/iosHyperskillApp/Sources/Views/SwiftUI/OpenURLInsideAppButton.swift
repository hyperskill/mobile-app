import shared
import SwiftUI

struct OpenURLInsideAppButton: View {
    private let text: String

    private let url: URL

    private let webControllerManager: WebControllerManager
    private let webControllerKey: WebControllerManager.WebControllerKey
    private let webControllerType: WebControllerManager.WebControllerType
    private let webControllerBackButtonStyle: WebControllerManager.BackButtonStyle

    private let onTap: (() -> Void)?

    init(
        text: String,
        url: URL,
        webControllerManager: WebControllerManager = .shared,
        webControllerKey: WebControllerManager.WebControllerKey = .externalLink,
        webControllerType: WebControllerManager.WebControllerType,
        webControllerBackButtonStyle: WebControllerManager.BackButtonStyle = .done,
        onTap: (() -> Void)? = nil
    ) {
        self.text = text
        self.url = url
        self.webControllerManager = webControllerManager
        self.webControllerKey = webControllerKey
        self.webControllerType = webControllerType
        self.webControllerBackButtonStyle = webControllerBackButtonStyle
        self.onTap = onTap
    }

    var body: some View {
        Button(text) {
            onTap?()

            webControllerManager.presentWebControllerWithURL(
                url,
                withKey: webControllerKey,
                controllerType: webControllerType,
                backButtonStyle: webControllerBackButtonStyle
            )
        }
    }
}

struct URLButton_Previews: PreviewProvider {
    static var previews: some View {
        OpenURLInsideAppButton(
            text: "URL button",
            url: URL(string: "https://www.google.com/").require(),
            webControllerType: .inAppSafari
        )
    }
}
