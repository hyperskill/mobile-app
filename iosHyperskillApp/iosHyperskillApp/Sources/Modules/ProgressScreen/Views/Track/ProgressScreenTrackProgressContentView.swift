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

    let onChangeTrackTap: () -> Void

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

            Button(Strings.ProgressScreen.Track.changeTrack) {
                onChangeTrackTap()
            }
            .font(.subheadline)
            .frame(maxWidth: .infinity)
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
                imageResource: .topic,
                progress: .init(value: completedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.ProgressScreen.Track.completedTopics
            )

            ProgressScreenCardView(
                appearance: appearance.cardAppearance,
                title: appliedTopicsCountLabel,
                titleSecondaryText: appliedTopicsPercentageLabel,
                imageResource: .hammer,
                progress: .init(value: appliedTopicsPercentageProgress, isCompleted: isCompleted),
                subtitle: Strings.ProgressScreen.Track.appliedCoreTopics
            )

            HStack(alignment: .top, spacing: appearance.interitemSpacing) {
                if let timeToCompleteLabel {
                    ProgressScreenCardView(
                        appearance: appearance.cardAppearance,
                        title: timeToCompleteLabel,
                        titleSecondaryText: nil,
                        imageResource: .stepTimeToComplete,
                        progress: nil,
                        subtitle: Strings.ProgressScreen.Track.timeToCompleteTrack
                    )
                }

                ProgressScreenCardView(
                    appearance: appearance.cardAppearance,
                    title: "\(completedGraduateProjectsCount)",
                    titleSecondaryText: nil,
                    imageResource: .projectSelectionListProjectGraduate,
                    imageRenderingMode: .original,
                    progress: nil,
                    subtitle: Strings.ProgressScreen.Track.completedGraduateProject
                )
            }
        }
    }
}

#Preview {
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
        completedTopicsPercentageLabel: "• 98%",
        completedTopicsPercentageProgress: 0,
        appliedTopicsCountLabel: "0 / 138",
        appliedTopicsPercentageLabel: "• 0%",
        appliedTopicsPercentageProgress: 0,
        timeToCompleteLabel: "~ 56 h",
        completedGraduateProjectsCount: 0,
        isCompleted: false,
        onChangeTrackTap: {}
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
