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

    let text: String

    let level: Int

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            ForEach(0..<level, id: \.self) { _ in
                StepQuizParsonsLevelView()
            }
            LatexView(
                text: .constant("<code style=\"\(getCodeStyle())\">\(text)</code>"),
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

    private func getCodeStyle() -> String {
        "border: 0; width: 100%; padding: 0; margin: 0"
    }
}

struct StepQuizParsonsItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizParsonsItemView(
                isSelected: true,
                text: "if b &lt; minimum:",
                level: 1
            )

            StepQuizParsonsItemView(
                isSelected: false,
                text: "if b &lt; minimum:",
                level: 0
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
