import shared
import SwiftUI

extension StepTheoryContentView {
    struct Appearance {
        let interItemSpacing: CGFloat = 24
    }
}

struct StepTheoryContentView: View {
    private(set) var appearance = Appearance()

    @State var viewData: StepViewData

    @State var isStepTextContentLoaded = false

    let startPracticingButton: StartPracticingButton?

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                buildStartPracticingButton(isFilled: false)

                StepTextView(
                    text: viewData.text,
                    onContentLoaded: {
                        isStepTextContentLoaded = true
                    }
                )

                buildStartPracticingButton(isFilled: true)
            }
            .padding()
        }
    }
}

// MARK: - StepTheoryContentView (StartPracticingButton) -

extension StepTheoryContentView {
    struct StartPracticingButton {
        let isLoading: Bool
        let action: () -> Void
    }

    @ViewBuilder
    private func buildStartPracticingButton(isFilled: Bool) -> some View {
        if let startPracticingButton, isStepTextContentLoaded {
            StepActionButton(
                title: Strings.Step.startPracticing,
                style: isFilled ? .violetFilled : .violetOutline,
                isLoading: startPracticingButton.isLoading,
                onClick: startPracticingButton.action
            )
        }
    }
}

#if DEBUG
struct StepTheoryContentView_Previews: PreviewProvider {
    static var previews: some View {
        StepTheoryContentView(
            viewData: .placeholder,
            startPracticingButton: StepTheoryContentView.StartPracticingButton(
                isLoading: false,
                action: {}
            )
        )
    }
}
#endif
