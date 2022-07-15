import SwiftUI

struct URLButton: View {
    let text: String

    let url: URL

    var body: some View {
        Button(text) {
            WebControllerManager.shared.presentWebControllerWithURL(
                url,
                withKey: .externalLink,
                allowsSafari: true,
                backButtonStyle: .done
            )
        }
    }
}

struct URLButton_Previews: PreviewProvider {
    static var previews: some View {
        if let url = HyperskillURLFactory.makeRegister() {
            URLButton(text: "URL button", url: url)
        }
    }
}
