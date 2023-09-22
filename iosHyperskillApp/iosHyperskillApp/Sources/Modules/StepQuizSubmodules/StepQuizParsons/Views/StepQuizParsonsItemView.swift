import SwiftUI

extension StepQuizParsonsItemView {
    struct Appearance {
        let tabTextFont = UIFont.monospacedSystemFont(ofSize: 14, weight: .regular)

        let borderWidth: CGFloat = 2
        let borderCornerRadius: CGFloat = 4
    }
}

struct StepQuizParsonsItemView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool
    let code: StepQuizParsonsViewData.CodeContent
    let level: Int

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: LayoutInsets.smallInset) {
                ForEach(0..<level, id: \.self) { _ in
                    Text(". . . .")
                        .font(Font(appearance.tabTextFont))
                        .foregroundColor(.tertiaryText)
                        .frame(height: appearance.tabTextFont.pointSize)
                }

                switch code {
                case .attributedString(let attributedText):
                    AttributedTextLabelWrapper(
                        attributedText: attributedText
                    )
                case .htmlText(let string):
                    LatexView(
                        text: .constant(string),
                        configuration: .quizContent(backgroundColor: .clear)
                    )
                    .frame(width: CGFloat(string.count * 15))
                }
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
                code: .htmlText("if b &lt; minimum:"),
                level: 1
            )

            StepQuizParsonsItemView(
                isSelected: false,
                code: .htmlText("if b &lt; minimum:"),
                level: 0
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
