import SwiftUI

struct StepQuizCodeBlanksActionButtonsView: View {
    let isDeleteButtonEnabled: Bool
    let isSpaceButtonHidden: Bool
    let isDecreaseIndentLevelButtonHidden: Bool

    let onSpaceTap: () -> Void
    let onDeleteTap: () -> Void
    let onEnterTap: () -> Void
    let onDecreaseIndentLevelTap: () -> Void

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            Spacer()

            if !isDecreaseIndentLevelButtonHidden {
                StepQuizCodeBlanksActionButton
                    .decreaseIndentLevel(action: onDecreaseIndentLevelTap)
            }

            if !isSpaceButtonHidden {
                StepQuizCodeBlanksActionButton
                    .space(action: onSpaceTap)
            }

            StepQuizCodeBlanksActionButton
                .delete(action: onDeleteTap)
                .disabled(!isDeleteButtonEnabled)

            StepQuizCodeBlanksActionButton
                .enter(action: onEnterTap)
        }
        .padding(.horizontal)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksActionButtonsView(
            isDeleteButtonEnabled: false,
            isSpaceButtonHidden: false,
            isDecreaseIndentLevelButtonHidden: false,
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {},
            onDecreaseIndentLevelTap: {}
        )

        StepQuizCodeBlanksActionButtonsView(
            isDeleteButtonEnabled: true,
            isSpaceButtonHidden: true,
            isDecreaseIndentLevelButtonHidden: true,
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {},
            onDecreaseIndentLevelTap: {}
        )
    }
    .padding()
}
#endif
