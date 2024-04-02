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

    let onTheoryFeedbackButtonTap: () -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(timeToComplete: viewData.formattedTimeToComplete)

                buildStartPracticingButton(isLocatedAtBeginning: true)

                LatexView(
                    text: viewData.text,
                    configuration: .stepText(),
                    onContentLoaded: {
                        isStepTextContentLoaded = true
                    }
                )

                buildStartPracticingButton(isLocatedAtBeginning: false)
            }
            .padding()
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(
                    action: onTheoryFeedbackButtonTap,
                    label: { Image(systemName: "flag") }
                )
                .disabled(!isStepTextContentLoaded)
            }
        }
    }
}

// MARK: - StepTheoryContentView (StartPracticingButton) -

extension StepTheoryContentView {
    struct StartPracticingButton {
        let isLoading: Bool
        let action: (Bool) -> Void
    }

    @ViewBuilder
    private func buildStartPracticingButton(isLocatedAtBeginning: Bool) -> some View {
        if let startPracticingButton, isStepTextContentLoaded {
            StepActionButton(
                title: Strings.Step.startPracticing,
                style: isLocatedAtBeginning ? .violetOutline : .violetFilled,
                isLoading: startPracticingButton.isLoading,
                onClick: {
                    startPracticingButton.action(isLocatedAtBeginning)
                }
            )
        }
    }
}

#if DEBUG
#Preview {
    StepTheoryContentView(
        viewData: .placeholder,
        startPracticingButton: StepTheoryContentView.StartPracticingButton(
            isLoading: false,
            action: { _ in }
        ),
        onTheoryFeedbackButtonTap: {}
    )
}
#endif
