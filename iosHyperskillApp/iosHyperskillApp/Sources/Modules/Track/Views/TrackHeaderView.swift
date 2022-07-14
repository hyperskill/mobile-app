import SwiftUI

extension TrackHeaderView {
    struct Appearance {
        let avatarImageWidthHeight: CGFloat = 34
    }
}

struct TrackHeaderView: View {
    private(set) var appearance = Appearance()

    let avatarSource: String?

    let title: String

    let subtitle: String

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            LazyAvatarView(avatarSource)
                .frame(widthHeight: appearance.avatarImageWidthHeight)

            VStack(spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.title3)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)

                Text(subtitle)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
                    .multilineTextAlignment(.center)
            }
        }
        .padding(.horizontal)
    }
}

struct TrackHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        TrackHeaderView(
            avatarSource: nil,
            title: "Python for Beginners",
            subtitle: "Learning now"
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
