import SwiftUI

extension StepQuizMatchingItemView {
    struct Appearance {
        let interItemSpacing: CGFloat = 20

        let itemHorizontalInset: CGFloat = 64
    }
}

struct StepQuizMatchingItemView: View {
    private(set) var appearance = Appearance()

    let title: String
    let option: String

    let isMoveUpEnabled: Bool
    let isMoveDownEnabled: Bool

    let onMoveUp: () -> Void
    let onMoveDown: () -> Void

    var body: some View {
        VStack(spacing: appearance.interItemSpacing) {
            HStack(spacing: 0) {
                Text(title)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
                    .animation(nil)

                Spacer(minLength: appearance.itemHorizontalInset)
            }

            HStack(spacing: 0) {
                Spacer(minLength: appearance.itemHorizontalInset)

                StepQuizSortingItemView(
                    text: option,
                    isMoveUpEnabled: isMoveUpEnabled,
                    isMoveDownEnabled: isMoveDownEnabled,
                    onMoveUp: onMoveUp,
                    onMoveDown: onMoveDown
                )
            }
        }
    }
}

#if DEBUG
struct StepQuizMatchingItemVIew_Previews: PreviewProvider {
    static var previews: some View {
        let item = StepQuizMatchingViewData.placeholder.items[0]
        Group {
            StepQuizMatchingItemView(
                title: item.title.text,
                option: item.option.text,
                isMoveUpEnabled: false,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizMatchingItemView(
                title: item.title.text,
                option: item.option.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizMatchingItemView(
                title: item.title.text,
                option: item.option.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: false,
                onMoveUp: {},
                onMoveDown: {}
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
#endif
