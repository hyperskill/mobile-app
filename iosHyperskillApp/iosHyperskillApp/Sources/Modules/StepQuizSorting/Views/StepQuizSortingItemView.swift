import SwiftUI

struct StepQuizSortingItemView: View {
    let text: String

    let isMoveUpEnabled: Bool

    let isMoveDownEnabled: Bool

    let onMoveUp: () -> Void

    let onMoveDown: () -> Void

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            LatexView(text: .constant(text), configuration: .quizContent())

            VStack(spacing: LayoutInsets.defaultInset) {
                StepQuizSortingIcon(direction: .up, onTap: onMoveUp)
                    .disabled(!isMoveUpEnabled)

                StepQuizSortingIcon(direction: .down, onTap: onMoveDown)
                    .disabled(!isMoveDownEnabled)
            }
        }
        .padding()
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12), cornerRadius: 6)
    }
}

struct StepQuizSortingItemView_Previews: PreviewProvider {
    static var previews: some View {
        let items = StepQuizSortingViewData.makePlaceholder().items

        let firstItem = items[0]

        let middleItem = items[1]

        let lastItem = items[items.count - 1]

        return Group {
            StepQuizSortingItemView(
                text: firstItem.text,
                isMoveUpEnabled: false,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: middleItem.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: lastItem.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: false,
                onMoveUp: {},
                onMoveDown: {}
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
