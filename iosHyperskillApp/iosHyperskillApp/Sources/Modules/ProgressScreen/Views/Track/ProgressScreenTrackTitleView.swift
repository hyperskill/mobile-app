import SwiftUI

extension ProgressScreenTrackTitleView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let avatarSize = CGSize(width: 34, height: 34)
    }
}

struct ProgressScreenTrackTitleView: View {
    private(set) var appearance = Appearance()

    let avatarImageSource: String?

    let title: String

    private var isEmpty: Bool {
        (avatarImageSource?.isEmpty ?? true) && title.isEmpty
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: appearance.spacing) {
                if let avatarImageSource {
                    LazyAvatarView(avatarImageSource)
                        .frame(size: appearance.avatarSize)
                }

                Text(title)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            }
        }
    }
}

struct ProgressScreenTrackTitleView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenTrackTitleView(
            avatarImageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            title: "Python Core"
        )
        .padding()
    }
}
