import shared
import SwiftUI

struct LeaderboardListView: View {
    let items: [LeaderboardWidgetListItem]

    var body: some View {
        List {
            ForEach(sectionedItems(), id: \.self) { items in
                Section {
                    ForEach(items, id: \.self) { item in
                        LeaderboardListRowView(item: item)
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

//#Preview {
//    LeaderboardListView()
//}
