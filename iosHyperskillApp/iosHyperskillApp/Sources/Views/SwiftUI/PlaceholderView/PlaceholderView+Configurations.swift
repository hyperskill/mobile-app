import SwiftUI

extension PlaceholderView.Configuration {
    static func networkError(
        backgroundColor: Color = Color(ColorPalette.surface),
        action: @escaping () -> Void
    ) -> Self {
        .init(
            image: .init(name: Images.Placeholder.networkError, frame: .size(CGSize(width: 122, height: 122))),
            title: .init(text: Strings.Placeholder.networkErrorTitle),
            button: .init(text: Strings.Placeholder.networkErrorButtonText, action: action),
            backgroundColor: backgroundColor
        )
    }
}
