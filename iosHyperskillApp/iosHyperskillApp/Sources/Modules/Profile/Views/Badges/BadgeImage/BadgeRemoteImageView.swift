import NukeUI
import shared
import SkeletonUI
import SwiftUI

struct BadgeRemoteImageView: View {
    let image: BadgeImageRemote

    private(set) var primarySourceType: Source = .full

    @State private var isLoading = false

    private var imageSource: String {
        switch primarySourceType {
        case .full:
            return image.fullSource
        case .preview:
            return image.previewSource
        }
    }

    var body: some View {
        ZStack {
            LazyImage(
                source: imageSource,
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
            image: BadgeImageRemote(
                fullSource: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png",
                previewSource: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png"
            )
        )
    }
}
