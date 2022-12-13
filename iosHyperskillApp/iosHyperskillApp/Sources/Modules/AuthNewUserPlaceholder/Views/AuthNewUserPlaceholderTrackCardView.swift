import NukeUI
import SwiftUI

extension AuthNewUserPlaceholderTrackCardView {
    struct Appearance {
        let trackImageWidthHeight: CGFloat = 20
        let starImageWidthHeight: CGFloat = 20
    }
}

struct AuthNewUserPlaceholderTrackCardView: View {
    private(set) var appearance = Appearance()

    let imageSource: String

    let title: String

    let timeToComplete: String

    let rating: String

    var body: some View {
        HStack(alignment: .top, spacing: LayoutInsets.defaultInset) {
            LazyImage(
                source: imageSource,
                resizingMode: .aspectFill
            )
            .frame(widthHeight: appearance.trackImageWidthHeight)

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.body)
                    .foregroundColor(.primaryText)

                Text(timeToComplete)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
            }

            Spacer()

            HStack(spacing: LayoutInsets.smallInset) {
                Image(systemName: "star.fill")
                    .renderingMode(.template)
                    .resizable()
                    .foregroundColor(Color(ColorPalette.overlayYellow))
                    .frame(widthHeight: appearance.starImageWidthHeight)

                Text(rating)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder()
    }
}

struct AuthNewUserPlaceholderTrackCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthNewUserPlaceholderTrackCardView(
                imageSource: "https://hyperskill.org/media/tracks/0580353297be4214ab9ed7be8ce75d3d/Kotlin.svg",
                title: "Kotlin Basics",
                timeToComplete: "136 hours",
                rating: "4.7"
            )

            AuthNewUserPlaceholderTrackCardView(
                imageSource: "https://hyperskill.org/media/tracks/0580353297be4214ab9ed7be8ce75d3d/Kotlin.svg",
                title: "Kotlin Basics",
                timeToComplete: "136 hours",
                rating: "4.7"
            )
            .preferredColorScheme(.dark)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
