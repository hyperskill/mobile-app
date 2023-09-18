import shared
import SwiftUI

extension BadgeDetailsModalView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.smallInset

        let badgeImageViewSize = CGSize(width: 172, height: 200)

        func levelViewAppearance() -> BadgeLevelView.Appearance {
            .init(rootSpacing: interitemSpacing, currentLevelFont: .headline, nextLevelFont: .headline)
        }
    }
}

struct BadgeDetailsModalView: View {
    private(set) var appearance = Appearance()

    private(set) var badgeDetailsViewState: BadgeDetailsViewState

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(badgeDetailsViewState.title)
                    .foregroundColor(.primaryText)
                    .font(.title2)
                    .bold()

                Text(badgeDetailsViewState.badgeDescription)
                    .foregroundColor(.secondaryText)
                    .font(.subheadline)
            }

            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                BadgeRankView(
                    text: badgeDetailsViewState.formattedRank,
                    rank: badgeDetailsViewState.rank
                )

                BadgeImageView(
                    kind: badgeDetailsViewState.kind,
                    image: badgeDetailsViewState.image
                )
                .frame(size: appearance.badgeImageViewSize)
            }
            .frame(maxWidth: .infinity, alignment: .center)

            BadgeLevelView(
                appearance: appearance.levelViewAppearance(),
                currentLevel: badgeDetailsViewState.formattedCurrentLevel,
                nextLevel: badgeDetailsViewState.formattedNextLevel,
                progress: badgeDetailsViewState.progress,
                description: badgeDetailsViewState.levelDescription
            )
        }
        .padding()
    }
}

struct BadgeDetailsModalView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeDetailsModalView(
            badgeDetailsViewState: .init(
                kind: .committedlearner,
                rank: .apprentice,
                title: "Project Mastery",
                formattedRank: "Apprentice",
                image: BadgeImageLocked(),
                badgeDescription: "Complete projects to upgrade this badge",
                levelDescription: "Complete 1 project to unlock this badge",
                formattedCurrentLevel: "Locked",
                formattedNextLevel: "Level 1",
                progress: 0,
                isLocked: true
            )
        )
    }
}
