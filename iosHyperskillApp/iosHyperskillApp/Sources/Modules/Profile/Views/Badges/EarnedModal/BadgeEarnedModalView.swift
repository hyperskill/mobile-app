import shared
import SwiftUI

extension BadgeEarnedModalView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.smallInset

        let badgeImageViewSize = CGSize(width: 172, height: 200)
    }
}

struct BadgeEarnedModalView: View {
    private(set) var appearance = Appearance()

    private(set) var earnedBadgeModalViewState: EarnedBadgeModalViewState

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                Text(earnedBadgeModalViewState.formattedRank)
                    .foregroundColor(Color(earnedBadgeModalViewState.rank.foregroundColor))
                    .font(.headline)

                BadgeImageView(
                    kind: earnedBadgeModalViewState.kind,
                    image: earnedBadgeModalViewState.image
                )
                .frame(size: appearance.badgeImageViewSize)
            }
            .frame(maxWidth: .infinity, alignment: .center)

            VStack(alignment: .center, spacing: appearance.interitemSpacing) {
                Text(earnedBadgeModalViewState.title)
                    .font(.headline)
                    .foregroundColor(.primaryText)

                Text(earnedBadgeModalViewState.description_)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
                    .multilineTextAlignment(.center)
            }
        }
        .padding()
    }
}

struct BadgeEarnedModalView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeEarnedModalView(
            earnedBadgeModalViewState: .init(
                kind: .committedlearner,
                rank: .apprentice,
                formattedRank: "Apprentice",
                image: BadgeImageLocked(),
                title: "Wow! You've reached level 1",
                description: "You've earned the Project Master badge by reaching level 1! Amazing job!"
            )
        )
    }
}
