import SwiftUI

extension StepQuizParsonsLevelView {
    struct Appearance {
        let textFont = Font.custom("Menlo", size: 14)
    }
}

struct StepQuizParsonsLevelView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            ForEach(0..<4) { _ in
                Text(".")
                    .font(appearance.textFont)
                    .foregroundColor(.disabledText)
            }
        }
    }
}

struct StepQuizParsonsLevelView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizParsonsLevelView()
    }
}
