import shared
import SwiftUI

struct StepQuizCodeBlanksIfStatementView: View {
    let children: [StepQuizCodeBlanksViewStateCodeBlockChildItem]

    let onChildTap: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                Text("if")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                ForEach(children, id: \.id) { child in
                    StepQuizCodeBlanksCodeBlockChildView(child: child, action: onChildTap)
                }

                Text(":")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)
            }
            .padding(.horizontal, LayoutInsets.defaultInset)
            .padding(.vertical, LayoutInsets.smallInset)
            .background(Color(ColorPalette.violet400Alpha7))
            .cornerRadius(StepQuizCodeBlanksAppearance.cornerRadius)
            .padding(.horizontal)
        }
        .scrollBounceBehaviorBasedOnSize(axes: .horizontal)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksIfStatementView(
            children: [
                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
            ],
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksIfStatementView(
            children: [
                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: "x")
            ],
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksIfStatementView(
            children: [
                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: false, value: "x"),
                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 1, isActive: true, value: nil)
            ],
            onChildTap: { _ in }
        )
    }
    .padding()
}
#endif
