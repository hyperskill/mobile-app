import shared
import SwiftUI

extension LeaderboardListRowView {
    struct Appearance {
        let avatarSize = CGSize(width: 40, height: 40)

        static func backgroundColor(for item: LeaderboardListItem) -> Color {
            item.isCurrentUser
              ? Color(UIColor.dynamic(light: ColorPalette.blue400Alpha7, dark: .tertiarySystemGroupedBackground))
              : Color.systemSecondaryGroupedBackground
        }

        static func trophyForegroundColor(for item: LeaderboardListItem) -> Color {
            switch item.position {
            case 1:
                Color(ColorPalette.yellow200)
            case 2:
                Color.tertiaryText
            case 3:
                Color(ColorPalette.overlayOrange)
            default:
                Color.clear
            }
        }
    }
}

struct LeaderboardListRowView: View {
    private(set) var appearance = Appearance()

    let item: LeaderboardListItem

    var body: some View {
        HStack {
            positionView
            Spacer()
            userView
            Spacer()
            passedProblemsView
        }
    }

    private var positionView: some View {
        HStack {
            Text("\(item.position)")
                .font(.footnote)
                .foregroundColor(.secondaryText)

            if (1...3).contains(item.position) {
                Image(systemName: "trophy")
                    .font(.body)
                    .foregroundColor(Appearance.trophyForegroundColor(for: item))
            }
        }
    }

    private var userView: some View {
        HStack {
            LazyAvatarView(item.userAvatar)
                .frame(size: appearance.avatarSize)

            Text(item.username)
                .font(.body)
                .foregroundColor(.primaryText)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var passedProblemsView: some View {
        VStack(alignment: .trailing) {
            Text("\(item.passedProblems)")
                .font(.footnote)
                .foregroundColor(.secondaryText)

            Text(item.passedProblemsSubtitle)
                .font(.caption)
                .foregroundColor(.tertiaryText)
        }
    }
}

#Preview {
    VStack {
        LeaderboardListRowView(
            item: LeaderboardListItem(
                position: 1,
                passedProblems: 100,
                passedProblemsSubtitle: "problems",
                userId: 1,
                userAvatar: "",
                username: "User 1",
                isCurrentUser: false
            )
        )

        LeaderboardListRowView(
            item: LeaderboardListItem(
                position: 2,
                passedProblems: 90,
                passedProblemsSubtitle: "problems",
                userId: 2,
                userAvatar: "",
                username: "User 2",
                isCurrentUser: false
            )
        )

        LeaderboardListRowView(
            item: LeaderboardListItem(
                position: 3,
                passedProblems: 50,
                passedProblemsSubtitle: "problems",
                userId: 3,
                userAvatar: "",
                username: "User 3",
                isCurrentUser: false
            )
        )

        LeaderboardListRowView(
            item: LeaderboardListItem(
                position: 4,
                passedProblems: 25,
                passedProblemsSubtitle: "problems",
                userId: 4,
                userAvatar: "",
                username: "User 4",
                isCurrentUser: true
            )
        )

        LeaderboardListRowView(
            item: LeaderboardListItem(
                position: 5,
                passedProblems: 10,
                passedProblemsSubtitle: "problems",
                userId: 5,
                userAvatar: "",
                username: "Looooooong username User Looooooong username Looooooong username",
                isCurrentUser: false
            )
        )
    }
    .padding()
}
