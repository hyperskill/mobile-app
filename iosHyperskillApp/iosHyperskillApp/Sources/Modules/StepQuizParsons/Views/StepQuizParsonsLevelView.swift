import SwiftUI

extension StepQuizParsonsLevelView {
    struct Appearance {
        let textFont = Font(UIFont.monospacedSystemFont(ofSize: 14, weight: .regular))
    }
}

struct StepQuizParsonsLevelView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Text(". . . .")
                .font(appearance.textFont)
                .foregroundColor(.disabledText)
        }
    }
}

struct StepQuizParsonsLevelView_Previews: PreviewProvider {
    static var previews: some View {
        HStack {
            ForEach(0..<4) { _ in
                StepQuizParsonsLevelView()
            }
        }
    }
}
