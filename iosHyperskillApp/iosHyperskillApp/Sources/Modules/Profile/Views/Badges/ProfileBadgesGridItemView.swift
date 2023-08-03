import NukeUI
import shared
import SwiftUI

extension ProfileBadgesGridItemView {
    struct Appearance {
        let badgeIconHeight: CGFloat = 86
        let lockIconWidthHeight: CGFloat = 12
        let levelContainerSpacing: CGFloat = 4
        let cornerRadius: CGFloat = ProfileView.Appearance().cornerRadius
        let backgroundColor = Color(ColorPalette.surface)
    }
}

struct ProfileBadgesGridItemView: View {
    private(set) var appearance = Appearance()

    let badge: BadgesViewState.BadgeViewState

    let onBadgeTapped: (BadgeKind) -> Void

    var body: some View {
        Button(
            action: {
                onBadgeTapped(badge.kind)
            },
            label: {
                VStack(spacing: LayoutInsets.defaultInset) {
                    Text(badge.title)
                        .font(.subheadline)
                        .foregroundColor(.primaryText)

                    buildImageView()
                        .frame(maxWidth: .infinity)
                        .frame(height: appearance.badgeIconHeight)

                    VStack(spacing: LayoutInsets.smallInset) {
                        HStack(spacing: appearance.levelContainerSpacing) {
                            Text(badge.formattedCurrentLevel)
                                .font(.caption2)
                                .foregroundColor(.primaryText)

                            Spacer()

                            if let nextLevel = badge.nextLevel {
                                Image(systemName: "lock")
                                    .resizable()
                                    .renderingMode(.template)
                                    .foregroundColor(.disabledText)
                                    .aspectRatio(contentMode: .fit)
                                    .frame(widthHeight: appearance.lockIconWidthHeight)

                                Text("\(nextLevel)")
                                    .font(.caption2)
                                    .foregroundColor(.disabledText)
                            }
                        }

                        ProfileBadgesLinearGradientProgressView(progress: badge.progress)
                    }
                }
                .padding()
                .background(appearance.backgroundColor)
                .cornerRadius(appearance.cornerRadius)
            }
        )
        .buttonStyle(BounceButtonStyle())
    }

    @ViewBuilder
    private func buildImageView() -> some View {
        switch BadgesViewStateBadgeImageKs(badge.image) {
        case .locked:
            Image(badge.kind.lockedImage)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
        case .remote(let remoteImage):
            ProfileBadgeImageView(source: remoteImage.fullSource)
        }
    }
}

fileprivate extension BadgeKind {
    var lockedImage: String {
        switch self {
        case .projectmaster:
            return Images.Profile.Badges.projectMastery
        case .topicmaster:
            return Images.Profile.Badges.topicMastery
        case .committedlearner:
            return Images.Profile.Badges.commitedLearning
        case .brilliantmind:
            return Images.Profile.Badges.brilliantMind
        case .helpinghand:
            return Images.Profile.Badges.helpingHand
        case .sweetheart:
            return Images.Profile.Badges.sweetheart
        case .benefactor:
            return Images.Profile.Badges.benefactor
        case .bountyhunter:
            return Images.Profile.Badges.bountyHunter
        default:
            return ""
        }
    }
}

#if DEBUG
struct ProfileBadgesListItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProfileBadgesGridItemView(
                badge: BadgesViewState.BadgeViewState.makePlaceholder(kind: .benefactor),
                onBadgeTapped: { _ in }
            )

            ProfileBadgesGridItemView(
                badge: BadgesViewState.BadgeViewState(
                    kind: .bountyhunter,
                    title: "Bounty Hunter",
                    image: BadgesViewStateBadgeImageRemote(
                        fullSource: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png",
                        previewSource: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png"
                    ),
                    formattedCurrentLevel: "Level 2",
                    nextLevel: 3,
                    progress: 0.3
                ),
                onBadgeTapped: { _ in }
            )
        }
        .frame(maxWidth: 180)
        .previewLayout(.sizeThatFits)
    }
}

extension BadgesViewState.BadgeViewState {
    static func makePlaceholder(kind: BadgeKind) -> BadgesViewState.BadgeViewState {
        BadgesViewState.BadgeViewState(
            kind: kind,
            title: "Bounty Hunter",
            image: BadgesViewStateBadgeImageLocked(),
            formattedCurrentLevel: "Level 0",
            nextLevel: 1,
            progress: 0.3
        )
    }
}
#endif
