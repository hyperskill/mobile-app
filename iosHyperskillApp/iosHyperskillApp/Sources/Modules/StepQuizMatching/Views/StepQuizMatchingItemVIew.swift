import SwiftUI

struct StepQuizMatchingItemVIew: View {
    var titleText: String
    var optionText: String
    var onMoveUp: (() -> Void)?
    var onMoveDown: (() -> Void)?

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            HStack {
                Text(titleText)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
                Spacer(minLength: 70)
            }

            HStack {
                Spacer(minLength: 60)
                ItemWithChevrons(onMoveUp: onMoveUp, onMoveDown: onMoveDown, content: {
                    Text(optionText)
                        .font(.body)
                        .foregroundColor(.primaryText)
                        .multilineTextAlignment(.leading)
                        .frame(maxWidth: .infinity, alignment: .leading)
                })
            }
        }
        .frame(width: .infinity)
    }
}

struct StepQuizMatchingItemVIew_Previews: PreviewProvider {
    static var previews: some View {
        let item = StepQuizMatchingViewData.makePlaceholder().items[0]
        Group {
            StepQuizMatchingItemVIew(
                titleText: item.title.text,
                optionText: item.option.text,
                onMoveUp: nil,
                onMoveDown: {}
            )

            StepQuizMatchingItemVIew(
                titleText: item.title.text,
                optionText: item.option.text,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizMatchingItemVIew(
                titleText: item.title.text,
                optionText: item.option.text,
                onMoveUp: {},
                onMoveDown: nil
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
