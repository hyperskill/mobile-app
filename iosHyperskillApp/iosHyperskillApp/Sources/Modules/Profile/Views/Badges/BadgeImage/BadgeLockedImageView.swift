import shared
import SwiftUI

struct BadgeLockedImageView: View {
    let kind: BadgeKind

    var body: some View {
        Image(kind.lockedImage)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
    }
}

private extension BadgeKind {
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
            assertionFailure("BadgeLockedImageView :: unknown badge kind")
            return ""
        }
    }
}

struct BadgeLockedImageView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeLockedImageView(kind: .benefactor)
    }
}
