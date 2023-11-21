import SwiftUI

struct ChallengeWidgetErrorView: View {
    let backgroundColor: Color

    let action: () -> Void

    var body: some View {
        PlaceholderView(
            configuration: .init(
                presentationMode: .local,
                image: .reload,
                title: .init(text: Strings.ChallengeWidget.networkError),
                button: .init(text: Strings.Placeholder.networkErrorButtonText, action: action, style: .outline()),
                primaryContentAlignment: .horizontal(),
                interItemSpacing: LayoutInsets.defaultInset,
                backgroundColor: .clear
            )
        )
        .padding()
        .background(backgroundColor)
        .addBorder()
    }
}

#Preview {
    ChallengeWidgetErrorView(
        backgroundColor: .systemBackground,
        action: {}
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
