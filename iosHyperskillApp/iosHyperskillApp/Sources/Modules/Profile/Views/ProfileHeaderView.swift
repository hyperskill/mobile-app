import SwiftUI

extension ProfileHeaderView {
    struct Appearance {
        let avatarImageWidthHeight: CGFloat = 64
    }
}

struct ProfileHeaderView: View {
    private(set) var appearance = Appearance()

    let avatarSource: String?

    let title: String

    let subtitle: String

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            ProfileAvatarView(avatarSource)
                .frame(widthHeight: appearance.avatarImageWidthHeight)

            VStack(alignment: .leading, spacing: 0) {
                Text(title)
                    .font(.title2)
                    .foregroundColor(.primaryText)

                Spacer()

                Text(subtitle)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }
}

struct ProfileHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileHeaderView(
            avatarSource: nil,
            title: "Konstantin Konstantinopolsky",
            subtitle: "JetBrains Academy Team"
        )
        .previewLayout(.sizeThatFits)
    }
}
