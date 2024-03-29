import SwiftUI

extension ProgressScreenProjectProgressContentView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat

        let avatarAppearance = ProjectLevelAvatarView.Appearance(
            imageSize: .init(width: 16, height: 16),
            padding: .small
        )

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

struct ProgressScreenProjectProgressContentView: View {
    let appearance: Appearance

    let projectLevel: SharedProjectLevelWrapper?

    let title: String

    let timeToCompleteLabel: String?

    let completedStagesLabel: String
    let completedStagesProgress: Float

    let isCompleted: Bool

    let onChangeProjectTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenSectionTitleView(
                appearance: .init(spacing: appearance.spacing),
                title: title
            ) {
                if let projectLevel {
                    ProjectLevelAvatarView(
                        appearance: appearance.avatarAppearance,
                        level: projectLevel
                    )
                }
            }

            HStack(alignment: .top, spacing: appearance.interitemSpacing) {
                if let timeToCompleteLabel {
                    ProgressScreenCardView(
                        appearance: appearance.cardAppearance,
                        title: timeToCompleteLabel,
                        titleSecondaryText: nil,
                        imageResource: .stepTimeToComplete,
                        progress: nil,
                        subtitle: Strings.ProgressScreen.Project.timeToCompleteProject
                    )
                }

                ProgressScreenCardView(
                    appearance: appearance.cardAppearance,
                    title: completedStagesLabel,
                    titleSecondaryText: nil,
                    imageResource: nil,
                    progress: .init(value: completedStagesProgress, isCompleted: isCompleted),
                    subtitle: Strings.ProgressScreen.Project.stages
                )
            }

            Button(Strings.ProgressScreen.Project.changeProject) {
                onChangeProjectTap()
            }
            .font(.subheadline)
            .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenProjectProgressContentView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenProjectProgressContentView(
            appearance: .init(
                spacing: LayoutInsets.defaultInset,
                interitemSpacing: LayoutInsets.smallInset,
                cardBackgroundColor: Color(ColorPalette.surface),
                cardCornerRadius: 8
            ),
            projectLevel: .easy,
            title: "Simple Chatty Bot",
            timeToCompleteLabel: "~ 56 h",
            completedStagesLabel: "0 / 5",
            completedStagesProgress: 0,
            isCompleted: false,
            onChangeProjectTap: {}
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
