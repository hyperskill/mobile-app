import SwiftUI

extension StarRatingView {
    struct Appearance {
        var imageSize = CGSize(width: 16, height: 16)
        var imageColor = Color(ColorPalette.overlayYellow)

        var textFont = Font.subheadline
        var textColor = Color.primaryText
    }
}

struct StarRatingView: View {
    private(set) var appearance = Appearance()

    let rating: RatingValue

    var body: some View {
        if let ratingText = rating.text {
            HStack(spacing: LayoutInsets.smallInset) {
                Image(systemName: "star.fill")
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .foregroundColor(appearance.imageColor)
                    .frame(size: appearance.imageSize)

                Text(ratingText)
                    .font(appearance.textFont)
                    .foregroundColor(appearance.textColor)
            }
        } else {
            EmptyView()
        }
    }

    enum RatingValue {
        case number(value: Double, decimalPoints: Int = 2)
        case string(String)

        fileprivate var text: String? {
            switch self {
            case .number(let value, let decimalPoints):
                return value > 0 ? Formatter.averageRating(value, decimalPoints: decimalPoints) : nil
            case .string(let value):
                return value.trimmedNonEmptyOrNil()
            }
        }
    }
}

struct StarRatingView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            StarRatingView(rating: .number(value: 4.77))
            StarRatingView(rating: .number(value: 4.7))

            StarRatingView(rating: .number(value: 4.77, decimalPoints: 1))
            StarRatingView(rating: .number(value: 4.7, decimalPoints: 1))

            StarRatingView(rating: .string("4.7"))
            StarRatingView(rating: .string(" "))
        }
    }
}
