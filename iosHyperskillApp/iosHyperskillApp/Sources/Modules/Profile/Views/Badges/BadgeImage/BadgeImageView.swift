import shared
import SwiftUI

struct BadgeImageView: View {
    let kind: BadgeKind
    let image: BadgeImage

    var body: some View {
        switch BadgeImageKs(image) {
        case .locked:
            BadgeLockedImageView(kind: kind)
        case .remote(let remoteImage):
            BadgeRemoteImageView(image: remoteImage)
        }
    }
}

extension BadgeImageView {
    init(badge: BadgesViewState.Badge) {
        self.init(kind: badge.kind, image: badge.image)
    }
}

struct ProfileBadgeImageView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeImageView(
            kind: .committedlearner,
            image: BadgeImageLocked()
        )
    }
}
