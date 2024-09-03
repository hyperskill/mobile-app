import SwiftUI

struct StepQuizCodeBlanksActionButtonsView: View {
    let isDeleteButtonEnabled: Bool
    let isSpaceButtonHidden: Bool

    let onSpaceTap: () -> Void
    let onDeleteTap: () -> Void
    let onEnterTap: () -> Void

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            Spacer()

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
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {}
        )

        StepQuizCodeBlanksActionButtonsView(
            isDeleteButtonEnabled: true,
            isSpaceButtonHidden: true,
            onSpaceTap: {},
            onDeleteTap: {},
            onEnterTap: {}
        )
    }
    .padding()
}
#endif
