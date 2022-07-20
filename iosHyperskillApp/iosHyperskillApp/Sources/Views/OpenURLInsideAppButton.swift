import SwiftUI

struct OpenURLInsideAppButton: View {
    private let text: String

    private let url: URL

    private let webControllerManager: WebControllerManager
    private let webControllerKey: WebControllerManager.WebControllerKey
    private let webControllerAllowsSafari: Bool
    private let webControllerBackButtonStyle: WebControllerManager.BackButtonStyle

    init(
        text: String,
        url: URL,
        webControllerManager: WebControllerManager = .shared,
        webControllerKey: WebControllerManager.WebControllerKey = .externalLink,
        webControllerAllowsSafari: Bool = true,
        webControllerBackButtonStyle: WebControllerManager.BackButtonStyle = .done
    ) {
        self.text = text
        self.url = url
        self.webControllerManager = webControllerManager
        self.webControllerKey = webControllerKey
        self.webControllerAllowsSafari = webControllerAllowsSafari
        self.webControllerBackButtonStyle = webControllerBackButtonStyle
    }

    var body: some View {
        Button(text) {
            webControllerManager.presentWebControllerWithURL(
                url,
                withKey: webControllerKey,
                allowsSafari: webControllerAllowsSafari,
                backButtonStyle: webControllerBackButtonStyle
            )
        }
    }
}

struct URLButton_Previews: PreviewProvider {
    static var previews: some View {
        if let url = HyperskillURLFactory.makeRegister() {
            OpenURLInsideAppButton(text: "URL button", url: url)
        }
    }
}
