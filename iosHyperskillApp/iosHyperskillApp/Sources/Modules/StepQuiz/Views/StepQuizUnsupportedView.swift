import SwiftUI

extension StepQuizUnsupportedView {
    enum Appearance {
        static let illustrationMaxHeight: CGFloat = 130

        static let rootSpacing = LayoutInsets.defaultInset * 2
        static let labelsSpacing = LayoutInsets.smallInset
        static let buttonsSpacing = LayoutInsets.defaultInset
    }
}

struct StepQuizUnsupportedView: View {
    var body: some View {
        VStack(alignment: .leading, spacing: Appearance.rootSpacing) {
            Image(.stepQuizUnsupportedIllustration)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: Appearance.illustrationMaxHeight,
                    alignment: .leading
                )

            VStack(alignment: .leading, spacing: Appearance.labelsSpacing) {
                Text(Strings.StepQuiz.unsupportedTitle)
                    .font(.title2.bold())

                Text(Strings.StepQuiz.unsupportedDescription)
                    .font(.body)
            }
            .foregroundColor(.newPrimaryText)
            .multilineTextAlignment(.leading)

            VStack(spacing: Appearance.buttonsSpacing) {
                Button(
                    Strings.StepQuiz.unsupportedButtonSolve,
                    action: {}
                )
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(
                    Strings.Common.goToStudyPlan,
                    action: {}
                )
                .buttonStyle(OutlineButtonStyle(style: .violet))
            }
        }
    }
}

#if DEBUG
#Preview {
    StepQuizUnsupportedView()
        .padding()
}
#endif
