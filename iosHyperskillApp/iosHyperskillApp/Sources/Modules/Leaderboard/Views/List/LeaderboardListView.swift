import shared
import SwiftUI

struct LeaderboardListView: View {
    let items: [LeaderboardWidgetListItem]

    let onRowTap: (LeaderboardListItem) -> Void

    var body: some View {
        List {
            ForEach(sectionedItems(), id: \.self) { items in
                Section {
                    ForEach(items, id: \.self) { item in
                        LeaderboardListRowView(
                            item: item,
                            onTap: { onRowTap(item) }
                        )
                        .listRowBackground(
                            LeaderboardListRowView.Appearance.backgroundColor(for: item)
                        )
                    }
                }
            }
        }
    }

    private func sectionedItems() -> [[LeaderboardListItem]] {
        var result = [[LeaderboardListItem]]()
        var currentSection = [LeaderboardListItem]()

        for item in items {
            switch LeaderboardWidgetListItemKs(item) {
            case .separator:
                if !currentSection.isEmpty {
                    result.append(currentSection)
                    currentSection = []
                }
            case .userInfo(let data):
                currentSection.append(LeaderboardListItem(sharedListItem: data))
            }
        }

        if !currentSection.isEmpty {
            result.append(currentSection)
        }

        return result
    }
}

#Preview {
    LeaderboardListView(
        items: [
            LeaderboardWidgetListItemUserInfo(
                position: 1,
                passedProblems: 203,
                passedProblemsSubtitle: "problems",
                userId: 1,
                userAvatar: "",
                username: "User 1",
                isCurrentUser: false
            ),
            LeaderboardWidgetListItemUserInfo(
                position: 2,
                passedProblems: 199,
                passedProblemsSubtitle: "problems",
                userId: 2,
                userAvatar: "",
                username: "User 2",
                isCurrentUser: false
            ),
            LeaderboardWidgetListItemUserInfo(
                position: 3,
                passedProblems: 141,
                passedProblemsSubtitle: "problems",
                userId: 3,
                userAvatar: "",
                username: "User 3",
                isCurrentUser: false
            ),
            LeaderboardWidgetListItemSeparator(),
            LeaderboardWidgetListItemUserInfo(
                position: 1203,
                passedProblems: 10,
                passedProblemsSubtitle: "problems",
                userId: 12,
                userAvatar: "",
                username: "User 1203",
                isCurrentUser: false
            ),
            LeaderboardWidgetListItemUserInfo(
                position: 1204,
                passedProblems: 10,
                passedProblemsSubtitle: "problems",
                userId: 13,
                userAvatar: "",
                username: "User 13",
                isCurrentUser: true
            ),
            LeaderboardWidgetListItemUserInfo(
                position: 1205,
                passedProblems: 10,
                passedProblemsSubtitle: "problems",
                userId: 14,
                userAvatar: "",
                username: "User 14",
                isCurrentUser: false
            ),
            LeaderboardWidgetListItemUserInfo(
                position: 1206,
                passedProblems: 9,
                passedProblemsSubtitle: "problems",
                userId: 15,
                userAvatar: "",
                username: "User 15",
                isCurrentUser: false
            )
        ],
        onRowTap: { _ in }
    )
}
