import SwiftUI

struct StepQuizSortingItemView: View {
    var text: String

    var position: Int

    var minPosition: Int = 0

    var maxPosition: Int

    var moveUp: (Int) -> Void

    var moveDown: (Int) -> Void

    var body: some View {
        HStack {
            Text(text)
                .font(.body)
                .foregroundColor(.primaryText)


            Spacer()
            VStack {
                StepQuizSortingIcon(
                    disabled: position == minPosition,
                    onClick: { moveUp(position) }
                )
                    .rotationEffect(.degrees(90))


                Spacer()

                StepQuizSortingIcon(
                    disabled: position == maxPosition,
                    onClick: { moveDown(position) }
                )
                    .rotationEffect(.degrees(-90))
            }
        }
        .frame(height: 92)
        .padding()
        .overlay(
            RoundedRectangle(cornerRadius: 6)
                .stroke(Color(ColorPalette.onSurfaceAlpha12))
        )
    }
}

struct StepQuizSortingItemView_Previews: PreviewProvider {
    static var previews: some View {
        let items = StepQuizSortingViewData.makePlaceholder().items

        let firstItem = items[0]

        let middleItem = items[1]

        let lastItem = items[items.capacity - 1]

        let lambda: (Int) -> Void = { _ in }

        return Group {
            StepQuizSortingItemView(
                text: firstItem.text,
                position: 0,
                maxPosition: items.capacity - 1,
                moveUp: lambda,
                moveDown: lambda
            )

            StepQuizSortingItemView(
                text: middleItem.text,
                position: 1,
                maxPosition: items.capacity - 1,
                moveUp: lambda,
                moveDown: lambda
            )

            StepQuizSortingItemView(
                text: lastItem.text,
                position: items.capacity - 1,
                maxPosition: items.capacity - 1,
                moveUp: lambda,
                moveDown: lambda
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
