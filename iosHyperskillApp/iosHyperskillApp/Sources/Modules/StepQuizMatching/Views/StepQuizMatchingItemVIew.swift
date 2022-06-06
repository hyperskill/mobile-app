import SwiftUI

struct StepQuizMatchingItemVIew: View {
    var titleText: String
    var optionText: String
    var onMoveUp: () -> Void
    var onMoveDown: () -> Void
    var isMoveUpEnabled: Bool
    var isMoveDownEnabled: Bool

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            GeometryReader { proxy in
                Text(titleText)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
                    .animation(nil)
                    .frame(
                        width: proxy.size.width * 0.9
                    )
            }
            .frame(height: 50)

            HStack {
                GeometryReader { proxy in
                    HStack {
                        Spacer()
                        StepQuizSortingItemView(
                            text: optionText,
                            isMoveUpEnabled: isMoveUpEnabled,
                            isMoveDownEnabled: isMoveDownEnabled,
                            onMoveUp: onMoveUp,
                            onMoveDown: onMoveDown
                        )
                        .frame(
                            width: proxy.size.width * 0.9
                        )
                    }
                }
                .frame(height: 92)
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
                onMoveUp: {},
                onMoveDown: {},
                isMoveUpEnabled: false,
                isMoveDownEnabled: true
            )

            StepQuizMatchingItemVIew(
                titleText: item.title.text,
                optionText: item.option.text,
                onMoveUp: {},
                onMoveDown: {},
                isMoveUpEnabled: true,
                isMoveDownEnabled: true
            )

            StepQuizMatchingItemVIew(
                titleText: item.title.text,
                optionText: item.option.text,
                onMoveUp: {},
                onMoveDown: {},
                isMoveUpEnabled: true,
                isMoveDownEnabled: false
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
