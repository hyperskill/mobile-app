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
                icon: .init(imageName: Images.Common.project, renderingMode: .circleBackground),
                title: "\(passedProjectsCount)",
                subtitle: Strings.Profile.Statistics.passedProjects
            )

            ProfileStatisticsItemView(
                appearance: .init(cornerRadius: appearance.cornerRadius),
                icon: .init(imageName: Images.Track.track, renderingMode: .circleBackground),
                title: "\(passedTracksCount)",
                subtitle: Strings.Profile.Statistics.passedTracks
            )

            ProfileStatisticsItemView(
                appearance: .init(cornerRadius: appearance.cornerRadius),
                icon: .init(imageName: Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge, renderingMode: .original),
                title: "\(hypercoinsBalance)",
                subtitle: Strings.Profile.Statistics.hypercoinsBalance
            )
        }
    }
}

struct ProfileStatisticsView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileStatisticsView(
            passedProjectsCount: 3,
            passedTracksCount: 3,
            hypercoinsBalance: 3456
        )
        .padding()
        .background(Color.background)
    }
}
