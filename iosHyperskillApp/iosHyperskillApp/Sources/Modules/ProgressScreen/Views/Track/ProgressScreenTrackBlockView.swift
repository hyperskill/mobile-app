import SwiftUI

extension ProgressScreenTrackBlockView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct ProgressScreenTrackBlockView: View {
    private(set) var appearance = Appearance()

    let avatarImageSource: String?
    let title: String

    let completedTopicsCountLabel: String
    let completedTopicsPercentageLabel: String
    let completedTopicsPercentageProgress: Float

    let appliedTopicsCountLabel: String
    let appliedTopicsPercentageLabel: String
    let appliedTopicsPercentageProgress: Float

    let timeToCompleteLabel: String?

    let completedGraduateProjectsCount: Int

    let isCompleted: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenTrackTitleView(
                avatarImageSource: avatarImageSource,
                title: title
            )

            buildCardsView()
        }
        .frame(maxWidth: .infinity)
    }

    @ViewBuilder
    private func buildCardsView() -> some View {
        VStack(spacing: appearance.interitemSpacing) {
            ProgressScreenTrackCardView(
                title: completedTopicsCountLabel,
                titleSecondaryText: completedTopicsPercentageLabel,
                imageName: Images.Track.About.topic,
                progress: .init(value: completedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.Track.Progress.completedTopics
            )

            ProgressScreenTrackCardView(
                title: appliedTopicsCountLabel,
                titleSecondaryText: appliedTopicsPercentageLabel,
                imageName: Images.Track.hammer,
                progress: .init(value: appliedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.Track.Progress.appliedCoreTopics
            )

            HStack(spacing: appearance.interitemSpacing) {
                if let timeToCompleteLabel {
                    ProgressScreenTrackCardView(
                        title: timeToCompleteLabel,
                        titleSecondaryText: nil,
                        imageName: Images.Step.clock,
                        progress: nil,
                        subtitle: Strings.Track.Progress.timeToComplete
                    )
                }

                ProgressScreenTrackCardView(
                    title: "\(completedGraduateProjectsCount)",
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
            avatarImageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            title: "Python Core",
            completedTopicsCountLabel: "0 / 149",
            completedTopicsPercentageLabel: "98%",
            completedTopicsPercentageProgress: 0,
            appliedTopicsCountLabel: "0 / 138",
            appliedTopicsPercentageLabel: "0%",
            appliedTopicsPercentageProgress: 0,
            timeToCompleteLabel: "~ 56 h",
            completedGraduateProjectsCount: 0,
            isCompleted: false
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
