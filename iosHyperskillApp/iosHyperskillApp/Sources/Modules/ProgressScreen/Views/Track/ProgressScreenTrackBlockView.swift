import SwiftUI

extension ProgressScreenTrackBlockView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct ProgressScreenTrackBlockView: View {
    private(set) var appearance = Appearance()

    let trackAvatarImageSource: String?
    let trackTitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenTrackTitleView(
                avatarImageSource: trackAvatarImageSource,
                title: trackTitle
            )

            buildCardsView()
        }
        .frame(maxWidth: .infinity)
    }

    @ViewBuilder
    private func buildCardsView() -> some View {
        VStack(spacing: appearance.interitemSpacing) {
            ProgressScreenTrackCardView(
                title: "48 / 149",
                titleSecondaryText: "• 32%",
                imageName: Images.Track.About.topic,
                progress: .init(value: 0.32, isCompleted: false),
                subtitle: Strings.Track.Progress.completedTopics
            )

            ProgressScreenTrackCardView(
                title: "0 / 138",
                titleSecondaryText: "• 0%",
                imageName: Images.Track.hammer,
                progress: .init(value: 0, isCompleted: false),
                subtitle: Strings.Track.Progress.appliedCoreTopics
            )

            HStack(spacing: appearance.interitemSpacing) {
                ProgressScreenTrackCardView(
                    title: "~ 56 h",
                    titleSecondaryText: nil,
                    imageName: Images.Step.clock,
                    progress: nil,
                    subtitle: Strings.Track.Progress.timeToComplete
                )

                ProgressScreenTrackCardView(
                    title: "0",
                    titleSecondaryText: nil,
                    imageName: Images.Track.projectGraduate,
                    imageRenderingMode: .original,
                    progress: nil,
                    subtitle: Strings.Track.Progress.completedGraduateProject
                )
            }
        }
    }
}

struct ProgressScreenTrackBlockView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenTrackBlockView(
            trackAvatarImageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            trackTitle: "Python Core"
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
