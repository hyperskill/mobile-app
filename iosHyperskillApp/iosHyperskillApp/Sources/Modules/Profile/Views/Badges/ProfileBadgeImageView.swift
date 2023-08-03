import NukeUI
import SwiftUI

struct ProfileBadgeImageView: View {
    let source: String

    @State private var isIconLoading = false

    var body: some View {
        ZStack {
            LazyImage(
                source: source,
                resizingMode: .aspectFit
            )
            .onStart { _ in
                isIconLoading = true
            }
            .onCompletion { _ in
                isIconLoading = false
            }

            EmptyView()
                .skeleton(with: isIconLoading)
                .shape(type: .circle)
                .appearance(type: .gradient())
                .animation(type: .linear(autoreverses: true))
        }
    }
}

struct ProfileBadgeImageView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileBadgeImageView(
            source: "https://hs-dev.azureedge.net/static/badges/apprentice-streak.png"
        )
    }
}
