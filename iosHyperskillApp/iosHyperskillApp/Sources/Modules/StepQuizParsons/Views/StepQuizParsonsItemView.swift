import SwiftUI

extension StepQuizParsonsItemView {
    struct Appearance {
        let cornerRadius: CGFloat = 4

        let isSelectedLineWidth: CGFloat = 2
        let isSelectedInset: CGFloat = 1
    }
}

struct StepQuizParsonsItemView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool

    let code: String

    let level: Int

    var styledCode: String {
        "<code style=\"border: 0; width: 100%; padding: 0; margin: 0\">\(code)</code>"
    }

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            ForEach(0..<level, id: \.self) { _ in
                StepQuizParsonsLevelView()
            }
            LatexView(
                text: .constant(styledCode),
                configuration: .quizContent()
            )
            .background(Color(ColorPalette.background))
        }
        .padding()
        .background(Color(ColorPalette.background))
        .cornerRadius(appearance.cornerRadius)
        .if(isSelected) { view in
            view.overlay(
                RoundedRectangle(cornerRadius: appearance.cornerRadius)
                    .inset(by: appearance.isSelectedInset)
                    .stroke(
                        Color(ColorPalette.primaryAlpha60),
                        lineWidth: appearance.isSelectedLineWidth
                    )
            )
        }
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
