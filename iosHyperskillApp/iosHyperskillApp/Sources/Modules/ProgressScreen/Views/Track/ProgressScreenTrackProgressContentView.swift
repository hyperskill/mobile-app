import SwiftUI

extension ProgressScreenTrackProgressContentView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat

        let avatarSize = CGSize(width: 34, height: 34)

        let cardBackgroundColor: Color
        let cardCornerRadius: CGFloat

        var cardAppearance: ProgressScreenCardView.Appearance {
            .init(
                spacing: spacing,
                interitemSpacing: interitemSpacing,
                backgroundColor: cardBackgroundColor,
                cornerRadius: cardCornerRadius
            )
        }
    }
}

struct ProgressScreenTrackProgressContentView: View {
    let appearance: Appearance

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
            ProgressScreenSectionTitleView(
                appearance: .init(spacing: appearance.spacing),
                title: title
            ) {
                LazyAvatarView(avatarImageSource)
                    .frame(size: appearance.avatarSize)
            }

            buildCardsView()
        }
        .frame(maxWidth: .infinity)
    }

    @ViewBuilder
    private func buildCardsView() -> some View {
        VStack(spacing: appearance.interitemSpacing) {
            ProgressScreenCardView(
                appearance: appearance.cardAppearance,
                title: completedTopicsCountLabel,
                titleSecondaryText: completedTopicsPercentageLabel,
                imageName: Images.Common.topic,
                progress: .init(value: completedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.ProgressScreen.Track.completedTopics
            )

            ProgressScreenCardView(
                appearance: appearance.cardAppearance,
                title: appliedTopicsCountLabel,
                titleSecondaryText: appliedTopicsPercentageLabel,
                imageName: Images.Common.hammer,
                progress: .init(value: appliedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.ProgressScreen.Track.appliedCoreTopics
            )

            HStack(spacing: appearance.interitemSpacing) {
                if let timeToCompleteLabel {
                    ProgressScreenCardView(
                        appearance: appearance.cardAppearance,
                        title: timeToCompleteLabel,
                        titleSecondaryText: nil,
                        imageName: Images.Step.clock,
                        progress: nil,
                        subtitle: Strings.ProgressScreen.Track.timeToCompleteTrack
                    )
                }

                ProgressScreenCardView(
                    appearance: appearance.cardAppearance,
                    title: "\(completedGraduateProjectsCount)",
                    titleSecondaryText: nil,
                    imageName: Images.ProjectSelectionList.projectGraduate,
                    imageRenderingMode: .original,
                    progress: nil,
                    subtitle: Strings.ProgressScreen.Track.completedGraduateProject
                )
            }
        }
    }
}

struct ProgressScreenTrackProgressContentView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenTrackProgressContentView(
            appearance: .init(
                spacing: LayoutInsets.defaultInset,
                interitemSpacing: LayoutInsets.smallInset,
                cardBackgroundColor: Color(ColorPalette.surface),
                cardCornerRadius: 8
            ),
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
