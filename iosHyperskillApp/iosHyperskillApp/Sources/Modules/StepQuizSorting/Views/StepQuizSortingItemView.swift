import SwiftUI

extension StepQuizSortingItemView {
    struct Appearance {
        let borderColor = Color(UIColor.dynamic(light: ColorPalette.onSurfaceAlpha12, dark: .separator))
    }
}

struct StepQuizSortingItemView: View {
    private(set) var appearance = Appearance()

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
        .addBorder(color: appearance.borderColor)
    }
}

struct StepQuizSortingItemView_Previews: PreviewProvider {
    static var previews: some View {
        let firstItem = StepQuizSortingViewData.Option(id: 0, text: "Byte")
        let secondItem = StepQuizSortingViewData.Option(id: 1, text: "Short")
        let thirdItem = StepQuizSortingViewData.Option(id: 2, text: "Long")

        return Group {
            StepQuizSortingItemView(
                text: firstItem.text,
                isMoveUpEnabled: false,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: secondItem.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: true,
                onMoveUp: {},
                onMoveDown: {}
            )

            StepQuizSortingItemView(
                text: thirdItem.text,
                isMoveUpEnabled: true,
                isMoveDownEnabled: false,
                onMoveUp: {},
                onMoveDown: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
