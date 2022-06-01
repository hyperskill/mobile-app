import SwiftUI

struct StepQuizSortingItemView: View {
    var text: String

    var position: Int

    var minPosition: Int = 0

    var maxPosition: Int

    var moveUp: () -> Void

    var moveDown: () -> Void

    var body: some View {
        HStack {
            Text(text)
                .font(.body)
                .foregroundColor(.primaryText)


            Spacer()
            VStack(spacing: 16) {
                StepQuizSortingIcon(
                    direction: .upward,
                    onClick: moveUp
                )
                    .disabled(position == minPosition)


                StepQuizSortingIcon(
                    direction: .down,
                    onClick: moveDown
                )
                    .disabled(position == maxPosition)
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
                position: 0,
                maxPosition: items.count - 1,
                moveUp: {},
                moveDown: {}
            )

            StepQuizSortingItemView(
                text: middleItem.text,
                position: 1,
                maxPosition: items.count - 1,
                moveUp: {},
                moveDown: {}
            )

            StepQuizSortingItemView(
                text: lastItem.text,
                position: items.count - 1,
                maxPosition: items.count - 1,
                moveUp: {},
                moveDown: {}
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
