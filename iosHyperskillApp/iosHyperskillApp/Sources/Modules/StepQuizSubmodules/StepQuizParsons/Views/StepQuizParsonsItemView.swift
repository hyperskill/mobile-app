import SwiftUI

extension StepQuizParsonsItemView {
    struct Appearance {
        let tabTextFont = Font(UIFont.monospacedSystemFont(ofSize: 14, weight: .regular))

        let borderWidth: CGFloat = 2
        let borderCornerRadius: CGFloat = 4
    }
}

struct StepQuizParsonsItemView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool
    let code: String
    let level: Int

    private var styledCode: String {
        "<code style=\"border: 0; padding: 0; margin: 0; background: transparent\">\(code)</code>"
    }

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: LayoutInsets.smallInset) {
                ForEach(0..<level, id: \.self) { _ in
                    Text(". . . .")
                        .font(appearance.tabTextFont)
                        .foregroundColor(.disabledText)
                }

                LatexView(
                    text: .constant(styledCode),
                    configuration: .quizContent(backgroundColor: .clear)
                )
                .frame(width: CGFloat(code.count * 15))
            }
        }
        .padding()
        .background(Color(ColorPalette.background))
        .addBorder(
            color: isSelected ? Color(ColorPalette.primaryAlpha60) : .clear,
            width: appearance.borderWidth,
            cornerRadius: appearance.borderCornerRadius
        )
    }
}

struct StepQuizParsonsItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizParsonsItemView(
                isSelected: true,
                code: "if b &lt; minimum:",
                level: 1
            )

            StepQuizParsonsItemView(
                isSelected: false,
                code: "if b &lt; minimum:",
                level: 0
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
