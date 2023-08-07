import NukeUI
import shared
import SwiftUI

struct ProfileBadgeImageView: View {
    let badge: BadgesViewState.Badge

    @State private var isLoading = false

    var body: some View {
        switch BadgesViewStateBadgeImageKs(badge.image) {
        case .locked:
            Image(badge.kind.lockedImage)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
        case .remote(let remoteImage):
            ZStack {
                LazyImage(
                    source: remoteImage.fullSource,
                    resizingMode: .aspectFit
                )
                .onStart { _ in
                    isLoading = true
                }
                .onCompletion { _ in
                    isLoading = false
                }

                EmptyView()
                    .skeleton(with: isLoading)
                    .shape(type: .circle)
                    .appearance(type: .gradient())
                    .animation(type: .linear(autoreverses: true))
            }
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
struct ProfileBadgeImageView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileBadgeImageView(badge: .makePlaceholder(kind: .committedlearner))
    }
}
#endif
