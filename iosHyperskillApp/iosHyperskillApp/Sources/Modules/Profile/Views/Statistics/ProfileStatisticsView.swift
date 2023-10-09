import SwiftUI

extension ProfileStatisticsView {
    struct Appearance {
        var cornerRadius: CGFloat = 8
    }
}

struct ProfileStatisticsView: View {
    private(set) var appearance = Appearance()

    let passedProjectsCount: Int

    let passedTracksCount: Int

    let hypercoinsBalance: Int

    var body: some View {
        HStack {
            ProfileStatisticsItemView(
                appearance: .init(cornerRadius: appearance.cornerRadius),
                icon: .init(imageResource: .project, renderingMode: .circleBackground),
                title: "\(passedProjectsCount)",
                subtitle: Strings.Profile.Statistics.passedProjects
            )

            ProfileStatisticsItemView(
                appearance: .init(cornerRadius: appearance.cornerRadius),
                icon: .init(imageResource: .track, renderingMode: .circleBackground),
                title: "\(passedTracksCount)",
                subtitle: Strings.Profile.Statistics.passedTracks
            )

            ProfileStatisticsItemView(
                appearance: .init(cornerRadius: appearance.cornerRadius),
                icon: .init(imageResource: .problemOfDaySolvedModalGemsBadge, renderingMode: .original),
                title: "\(hypercoinsBalance)",
                subtitle: Strings.Profile.Statistics.hypercoinsBalance
            )
        }
    }
}

#Preview {
    ProfileStatisticsView(
        passedProjectsCount: 3,
        passedTracksCount: 3,
        hypercoinsBalance: 3456
    )
    .padding()
    .background(Color.background)
}
