import NukeUI
import SwiftUI

extension AuthNewUserPlaceholderTrackCardView {
    struct Appearance {
        let trackAvatarSize = CGSize(width: 20, height: 20)

        let ratingViewAppearance = StarRatingView.Appearance(
            imageSize: CGSize(width: 20, height: 20),
            imageColor: Color(ColorPalette.overlayYellow),
            textFont: .subheadline,
            textColor: .primaryText
        )
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
            .frame(size: appearance.trackAvatarSize)

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.body)
                    .foregroundColor(.primaryText)

                Text(timeToComplete)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
            }

            Spacer()

            StarRatingView(
                appearance: appearance.ratingViewAppearance,
                rating: .string(rating)
            )
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
