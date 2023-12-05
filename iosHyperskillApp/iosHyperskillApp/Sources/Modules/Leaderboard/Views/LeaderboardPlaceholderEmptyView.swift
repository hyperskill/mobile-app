import SwiftUI

struct LeaderboardPlaceholderEmptyView: View {
    var body: some View {
        PlaceholderView(
            configuration: .imageAndTitle(
                image: PlaceholderView.Configuration.Image(
                    name: "leaderboard-placeholder-empty",
                    frame: .size(CGSize(width: 180, height: 164))
                ),
                titleText: Strings.Leaderboard.placeholderEmptyDescription,
                titleTextFont: .title2.bold(),
                backgroundColor: .clear
            )
        )
    }
}

#Preview {
    LeaderboardPlaceholderEmptyView()
}
