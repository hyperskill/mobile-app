import shared
import SwiftUI

extension ProfileBadgesGridView {
    struct Appearance {
        let defaultColumnsCount = 2
        let regularHorizontalSizeClassColumnsCount = 4

        func badgesColumnsCount(for horizontalSizeClass: UserInterfaceSizeClass?) -> Int {
            horizontalSizeClass == .regular ? regularHorizontalSizeClassColumnsCount : defaultColumnsCount
        }

        let rootSpacing: CGFloat = 16
        let interitemSpacing: CGFloat = 8
    }
}

struct ProfileBadgesGridView: View {
    private(set) var appearance = Appearance()

    let badgesState: BadgesViewState

    let onBadgeTapped: (BadgeKind) -> Void

    let onVisibilityButtonTap: (ProfileFeatureMessageBadgesVisibilityButton) -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        let badgesColumnsCount = appearance.badgesColumnsCount(for: horizontalSizeClass)

        VStack(spacing: appearance.rootSpacing) {
            HStack {
                Text(Strings.Profile.Badges.title)
                    .font(.headline)
                    .foregroundColor(.primaryText)

                Spacer()

                Button(
                    badgesState.isExpanded
                    ? Strings.Profile.Badges.showLess
                    : Strings.Profile.Badges.showAll
                ) {
                    onVisibilityButtonTap(
                        badgesState.isExpanded
                        ? ProfileFeatureMessageBadgesVisibilityButton.showLess
                        : ProfileFeatureMessageBadgesVisibilityButton.showAll
                    )
                }
                .buttonStyle(PrimaryTextButtonStyle())
            }

            LazyVGrid(
                columns: Array(
                    repeating: GridItem(
                        .flexible(),
                        spacing: appearance.interitemSpacing,
                        alignment: .top
                    ),
                    count: badgesColumnsCount
                ),
                alignment: .leading,
                spacing: appearance.interitemSpacing
            ) {
                ForEach(badgesState.badges, id: \.kind) { badge in
                    ProfileBadgesGridItemView(
                        badge: badge,
                        onBadgeTapped: { onBadgeTapped(badge.kind) }
                    )
                }
            }
        }
    }
}

struct ProfileBadgesListView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileBadgesGridView(
            badgesState: BadgesViewState(
                badges: [
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .benefactor),
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .bountyhunter),
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .sweetheart),
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .helpinghand),
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .brilliantmind),
                    BadgesViewState.BadgeViewState.makePlaceholder(kind: .committedlearner)
                ],
                isExpanded: true
            ),
            onBadgeTapped: { _ in },
            onVisibilityButtonTap: { _ in }
        )
        .padding()
        .background(Color(ColorPalette.background))
    }
}
