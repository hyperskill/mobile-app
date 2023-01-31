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

    @State var isStepTextLoaded = false

    let startPracticingButton: StartPracticingButton?

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                buildStartPracticingButton()
                    .buttonStyle(OutlineButtonStyle(style: .violet))

                StepTextView(
                    text: viewData.text,
                    onViewDidLoadContent: {
                        isStepTextLoaded = true
                    }
                )

                buildStartPracticingButton()
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }
            .padding()
        }
    }

    struct StartPracticingButton {
        let isLoading: Bool
        let action: () -> Void
    }

    @ViewBuilder
    private func buildStartPracticingButton() -> some View {
        if let startPracticingButton, isStepTextLoaded {
            Button(
                action: startPracticingButton.action,
                label: {
                    HStack(spacing: LayoutInsets.smallInset) {
                        if startPracticingButton.isLoading {
                            ProgressView()
                        }

                        Text(Strings.Step.startPracticing)
                    }
                }
            )
            .disabled(startPracticingButton.isLoading)
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
