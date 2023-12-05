import shared
import SwiftUI

struct LeaderboardListView: View {
    let items: [LeaderboardWidgetListItem]

    let updatesInText: String?

    let onRowTap: (LeaderboardListItem) -> Void

    var body: some View {
        List {
            ForEach(sections(), id: \.self) { section in
                Section(header: buildSectionHeader(for: section)) {
                    ForEach(section.items, id: \.self) { item in
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
        .listStyle(.insetGrouped)
    }

    @ViewBuilder
    private func buildSectionHeader(for sectionData: SectionData) -> some View {
        if let headerTitle = sectionData.headerTitle {
            Text(headerTitle)
        } else {
            EmptyView()
        }
    }

    private func sections() -> [SectionData] {
        var result = [SectionData]()
        var currentSectionItems = [LeaderboardListItem]()

        for item in items {
            switch LeaderboardWidgetListItemKs(item) {
            case .separator:
                if !currentSectionItems.isEmpty {
                    result.append(
                        SectionData(
                            headerTitle: result.isEmpty ? updatesInText : nil,
                            items: currentSectionItems
                        )
                    )
                    currentSectionItems = []
                }
            case .userInfo(let data):
                currentSectionItems.append(LeaderboardListItem(sharedListItem: data))
            }
        }

        if !currentSectionItems.isEmpty {
            result.append(
                SectionData(
                    headerTitle: result.isEmpty ? updatesInText : nil,
                    items: currentSectionItems
                )
            )
        }

        return result
    }

    private struct SectionData: Hashable {
        let headerTitle: String?
        let items: [LeaderboardListItem]
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
        updatesInText: "Update in 10 hours",
        onRowTap: { _ in }
    )
}
