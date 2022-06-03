import SwiftUI

struct StepQuizSortingItemView: View {
    var text: String

    var onMoveUp: (() -> Void)?

    var onMoveDown: (() -> Void)?

    var body: some View {
        ItemWithChevrons(onMoveUp: onMoveUp, onMoveDown: onMoveDown, content: {
            Text(text)
                .font(.body)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.leading)
                .frame(maxWidth: .infinity, alignment: .leading)
        })
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
                onMoveUp: nil,
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: middleItem.text,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: lastItem.text,
                onMoveUp: {},
                onMoveDown: nil
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
