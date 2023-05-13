import SwiftUI

extension RatingView {
    struct Appearance {
        var imageSize = CGSize(width: 16, height: 16)
        var imageColor = Color(ColorPalette.overlayYellow)

        var textFont = Font.subheadline
        var textColor = Color.primaryText
    }
}

struct RatingView: View {
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

struct RatingView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            RatingView(rating: .number(value: 4.77))
            RatingView(rating: .number(value: 4.7))

            RatingView(rating: .number(value: 4.77, decimalPoints: 1))
            RatingView(rating: .number(value: 4.7, decimalPoints: 1))

            RatingView(rating: .string("4.7"))
            RatingView(rating: .string(" "))
        }
    }
}
