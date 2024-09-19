import SwiftUI

struct StepQuizCodeBlanksActionButtonsView: View {
    let isDeleteButtonEnabled: Bool
    let isSpaceButtonHidden: Bool
    let isDecreaseIndentLevelButtonHidden: Bool

    let isDeleteButtonHighlightEffectActive: Bool
    let isEnterButtonHighlightEffectActive: Bool
    let isSpaceButtonHighlightEffectActive: Bool

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
                    .space(
                        isAnimationEffectActive: isSpaceButtonHighlightEffectActive,
                        action: onSpaceTap
                    )
            }

            StepQuizCodeBlanksActionButton
                .delete(
                    isAnimationEffectActive: isDeleteButtonHighlightEffectActive,
                    action: onDeleteTap
                )
                .disabled(!isDeleteButtonEnabled)

            StepQuizCodeBlanksActionButton
                .enter(
                    isAnimationEffectActive: isEnterButtonHighlightEffectActive,
                    action: onEnterTap
                )
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
            isDeleteButtonHighlightEffectActive: false,
            isEnterButtonHighlightEffectActive: false,
            isSpaceButtonHighlightEffectActive: true,
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {},
            onDecreaseIndentLevelTap: {}
        )

        StepQuizCodeBlanksActionButtonsView(
            isDeleteButtonEnabled: true,
            isSpaceButtonHidden: true,
            isDecreaseIndentLevelButtonHidden: true,
            isDeleteButtonHighlightEffectActive: true,
            isEnterButtonHighlightEffectActive: true,
            isSpaceButtonHighlightEffectActive: false,
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {},
            onDecreaseIndentLevelTap: {}
        )
    }
    .padding()
}
#endif
