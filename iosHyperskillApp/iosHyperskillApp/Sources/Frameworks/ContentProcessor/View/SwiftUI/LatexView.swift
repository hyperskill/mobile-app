import SwiftUI

struct LatexView: View {
    @Binding var text: String

    var configuration = SwiftUIProcessedContentView.Configuration()

    var onContentLoaded: (() -> Void)?

    var onOpenImageURL: ((URL) -> Void)?

    var onOpenLink: ((URL) -> Void)?

    @State private var height: CGFloat = 5

    var body: some View {
        SwiftUIProcessedContentView(
            text: $text,
            configuration: configuration,
            onContentLoaded: onContentLoaded,
            onHeightUpdated: { newHeight in
                height = CGFloat(newHeight)
            },
            onOpenImageURL: onOpenImageURL ?? openURLInTheWeb(_:),
            onOpenLink: onOpenLink ?? openURLInTheWeb(_:)
        )
        .frame(height: height)
        .frame(maxWidth: .infinity)
    }

    // MARK: Private API

    private func openURLInTheWeb(_ url: URL) {
        WebControllerManager.shared.presentWebControllerWithURL(
            url,
            withKey: .externalLink,
            controllerType: .inAppSafari
        )
    }
}

#Preview("Plain text") {
    LatexView(
        text: .constant("Plain text")
    )
    .padding()
}

#Preview("Rich text") {
    LatexView(
        text: .constant("Rich <b>text</b>!!!")
    )
    .padding()
}
