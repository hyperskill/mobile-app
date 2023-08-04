import shared
import SwiftUI

extension ProfileBadgesGridView {
    struct Appearance {
        var cornerRadius: CGFloat = 8

        let defaultColumnsCount = 2
        let regularHorizontalSizeClassColumnsCount = 4

        func badgesColumnsCount(for horizontalSizeClass: UserInterfaceSizeClass?) -> Int {
            horizontalSizeClass == .regular ? regularHorizontalSizeClassColumnsCount : defaultColumnsCount
        }
    }
}

struct ProfileBadgesGridView: View {
    private(set) var appearance = Appearance()

    let badgesState: BadgesViewState

    let onBadgeTap: (BadgeKind) -> Void
    let onVisibilityButtonTap: (ProfileFeatureMessageBadgesVisibilityButton) -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            HStack {
                Text(Strings.Profile.Badges.title)
                    .font(.headline)
                    .foregroundColor(.primaryText)

                Spacer()

                Button(
                    badgesState.isExpanded ? Strings.Profile.Badges.showLess : Strings.Profile.Badges.showAll
                ) {
                    onVisibilityButtonTap(badgesState.isExpanded ? .showLess : .showAll)
                }
                .font(.subheadline)
                .animation(.easeInOut, value: badgesState.isExpanded)
            }

            LazyVGrid(
                columns: Array(
                    repeating: GridItem(
                        .flexible(),
                        spacing: LayoutInsets.smallInset,
                        alignment: .top
                    ),
                    count: appearance.badgesColumnsCount(for: horizontalSizeClass)
                ),
                alignment: .leading,
                spacing: LayoutInsets.smallInset
            ) {
                ForEach(badgesState.badges, id: \.kind) { badge in
                    ProfileBadgesGridItemView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        badge: badge,
                        onBadgeTap: onBadgeTap
                    )
                }
            }
        }
    }
}

#if DEBUG
struct ProfileBadgesGridView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileBadgesGridView(
            badgesState: BadgesViewState(
                badges: [
                    .makePlaceholder(kind: .benefactor),
                    .makePlaceholder(kind: .bountyhunter),
                    .makePlaceholder(kind: .sweetheart),
                    .makePlaceholder(kind: .helpinghand),
                    .makePlaceholder(kind: .brilliantmind),
                    .makePlaceholder(kind: .committedlearner)
                ],
                isExpanded: true
            ),
            onBadgeTap: { _ in },
            onVisibilityButtonTap: { _ in }
        )
        .padding()
        .background(Color(ColorPalette.background))
    }
}
#endif
