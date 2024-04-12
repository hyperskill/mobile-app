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
                LatexView(
                    text: viewData.text,
                    configuration: .stepText(),
                    onContentLoaded: {
                        isStepTextContentLoaded = true
                    }
                )
            }
            .padding()
        }
        .safeAreaInsetBottomCompatibility(footer)
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

    @MainActor @ViewBuilder private var footer: some View {
        if let startPracticingButton, isStepTextContentLoaded {
            StepActionButton(
                title: Strings.Step.startPracticing,
                style: .violetFilled,
                isLoading: startPracticingButton.isLoading,
                onClick: {
                    FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                    startPracticingButton.action(false)
                }
            )
            .padding()
            .background(
                TransparentBlurView()
                    .edgesIgnoringSafeArea(.all)
            )
            .fixedSize(horizontal: false, vertical: true)
        } else {
            EmptyView()
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
