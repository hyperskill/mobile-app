import shared
import SwiftUI

extension ProfileBadgesGridItemView {
    struct Appearance {
        let badgeIconHeight: CGFloat = 86

        var cornerRadius: CGFloat = 8
        let backgroundColor = Color(ColorPalette.surface)
    }
}

struct ProfileBadgesGridItemView: View {
    private(set) var appearance = Appearance()

    let badge: BadgesViewState.Badge

    let onBadgeTap: (BadgeKind) -> Void

    var body: some View {
        Button(
            action: {
                onBadgeTap(badge.kind)
            },
            label: {
                VStack(spacing: LayoutInsets.defaultInset) {
                    Text(badge.title)
                        .font(.subheadline)
                        .foregroundColor(.primaryText)

                    BadgeImageView(badge: badge)
                        .frame(maxWidth: .infinity)
                        .frame(height: appearance.badgeIconHeight)

                    BadgeLevelView(
                        currentLevel: badge.formattedCurrentLevel,
                        nextLevel: badge.nextLevel?.intValue,
                        progress: badge.progress
                    )
                }
                .padding()
                .background(appearance.backgroundColor)
                .cornerRadius(appearance.cornerRadius)
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

#if DEBUG
struct ProfileBadgesGridItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProfileBadgesGridItemView(
                badge: .makePlaceholder(kind: .benefactor),
                onBadgeTap: { _ in }
            )

            ProfileBadgesGridItemView(
                badge: BadgesViewState.Badge(
                    kind: .bountyhunter,
                    title: "Bounty Hunter",
                    image: BadgeImageRemote(source: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png"),
                    formattedCurrentLevel: "Level 2",
                    nextLevel: 3,
                    progress: 0.3
                ),
                onBadgeTap: { _ in }
            )
        }
        .frame(maxWidth: 180)
        .previewLayout(.sizeThatFits)
    }
}

extension BadgesViewState.Badge {
    static func makePlaceholder(kind: BadgeKind) -> BadgesViewState.Badge {
        BadgesViewState.Badge(
            kind: kind,
            title: "Bounty Hunter",
            image: BadgeImageLocked(),
            formattedCurrentLevel: "Level 0",
            nextLevel: 1,
            progress: 0.3
        )
    }
}
#endif
