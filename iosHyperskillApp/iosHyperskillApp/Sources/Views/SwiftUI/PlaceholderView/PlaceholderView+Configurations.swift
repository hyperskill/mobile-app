import SwiftUI

extension PlaceholderView.Configuration {
    static let defaultImageSize = CGSize(width: 122, height: 122)

    static func networkError(
        titleText: String = Strings.Placeholder.networkErrorTitle,
        buttonText: String = Strings.Placeholder.networkErrorButtonText,
        backgroundColor: Color = Color(ColorPalette.surface),
        action: @escaping () -> Void
    ) -> Self {
        .init(
            image: .init(name: Images.Placeholder.networkError, frame: .size(defaultImageSize)),
            title: .init(text: titleText),
            button: .init(text: buttonText, action: action),
            backgroundColor: backgroundColor
        )
    }

    static func imageAndTitle(
        image: PlaceholderView.Configuration.Image = .init(
            name: Images.Placeholder.networkError,
            frame: .size(defaultImageSize)
        ),
        titleText: String,
        backgroundColor: Color = Color(ColorPalette.surface)
    ) -> Self {
        .init(
            image: image,
            title: .init(text: titleText),
            backgroundColor: backgroundColor
        )
    }
}
