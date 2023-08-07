import SwiftUI

extension BadgeDetailsModalView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.smallInset

        func levelViewAppearance() -> ProfileBadgeLevelView.Appearance {
            .init(rootSpacing: interitemSpacing, currentLevelFont: .headline, nextLevelFont: .headline)
        }
    }
}

struct BadgeDetailsModalView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .center, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text("Project Mastery")
                    .foregroundColor(.primaryText)
                    .font(.title2)
                    .bold()

                Text("Complete projects to upgrade this badge")
                    .foregroundColor(.secondaryText)
                    .font(.subheadline)
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                Text("Apprentice")
                    .foregroundColor(.disabledText)
                    .font(.headline)

                ProfileBadgeImageView(badge: .makePlaceholder(kind: .benefactor))
                    .frame(size: CGSize(width: 172, height: 200))
            }

            ProfileBadgeLevelView(
                appearance: appearance.levelViewAppearance(),
                currentLevel: "Locked",
                nextLevel: "Level 1",
                progress: 0,
                description: "Complete 1 project to unlock this badge"
            )
        }
        .padding([.horizontal, .bottom])
    }
}

struct BadgeDetailsModalView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeDetailsModalView()
    }
}
