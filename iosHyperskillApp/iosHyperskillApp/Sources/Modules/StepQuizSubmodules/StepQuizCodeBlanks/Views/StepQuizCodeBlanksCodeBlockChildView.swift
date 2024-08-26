import shared
import SwiftUI

struct StepQuizCodeBlanksCodeBlockChildView: View {
    let child: StepQuizCodeBlanksViewStateCodeBlockChildItem

    let action: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        childView(child: child)
            .onTapGesture {
                action(child)
            }
    }

    @ViewBuilder
    private func childView(
        child: StepQuizCodeBlanksViewStateCodeBlockChildItem
    ) -> some View {
        if let value = child.value {
            StepQuizCodeBlanksOptionView(text: value, isActive: child.isActive)
        } else {
            StepQuizCodeBlanksBlankView(style: .small, isActive: child.isActive)
        }
    }
}
