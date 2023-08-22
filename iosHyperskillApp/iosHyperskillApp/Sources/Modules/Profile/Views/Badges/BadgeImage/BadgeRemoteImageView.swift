import NukeUI
import shared
import SkeletonUI
import SwiftUI

struct BadgeRemoteImageView: View {
    let image: BadgeImageRemote

    @State private var isLoading = false

    var body: some View {
        ZStack {
            LazyImage(
                source: image.source,
                resizingMode: .aspectFit
            )
            .animation(nil)
            .onStart { task in
                if !ImagePipeline.shared.cache.containsData(for: task.request) {
                    isLoading = true
                }
            }
            .onCompletion { _ in
                isLoading = false
            }
            .opacity(isLoading ? 0 : 1)

            EmptyView()
                .skeleton(with: isLoading)
                .shape(type: .circle)
                .appearance(type: .gradient())
                .animation(type: .linear(autoreverses: true))
        }
    }

    enum Source {
        case full
        case preview
    }
}

struct BadgeRemoteImageView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeRemoteImageView(
            image: BadgeImageRemote(source: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png")
        )
    }
}
