import SwiftUI

extension ProjectSelectionListHeaderView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let avatarSize = CGSize(width: 40, height: 40)
    }
}

struct ProjectSelectionListHeaderView: View {
    private(set) var appearance = Appearance()

    let avatarSource: String?

    let title: String

    var body: some View {
        VStack(spacing: appearance.spacing) {
            if let avatarSource {
                LazyAvatarView(avatarSource)
                    .frame(size: appearance.avatarSize)
            }

            Text(title)
                .bold()
                .font(.title2)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)

            Text(Strings.ProjectSelectionList.List.description)
                .font(.body)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)
        }
    }
}

struct ProjectSelectionContentHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListHeaderView(
            avatarSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            title: "Select project in track Python Core"
        )
        .padding()
    }
}
