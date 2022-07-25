import shared
import SwiftUI

struct StepQuizActionButtons: View {
    var retryButton: RetryButton?

    let primaryButton: PrimaryButton

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            if let retryButton = retryButton {
                StepQuizRetryButton(
                    appearance: retryButton.appearance,
                    onTap: retryButton.action
                )
            }

            StepQuizActionButton(
                state: primaryButton.state,
                titleForState: primaryButton.titleForState,
                systemImageNameForState: primaryButton.systemImageNameForState,
                onTap: primaryButton.action
            )
        }
    }

    struct RetryButton {
        var appearance = StepQuizRetryButton.Appearance()

        let action: () -> Void
    }

    struct PrimaryButton {
        let state: StepQuizActionButton.State

        var titleForState: ((StepQuizActionButton.State) -> String?)?
        var systemImageNameForState: ((StepQuizActionButton.State) -> String?)?

        let action: () -> Void
    }
}

struct StepQuizActionButtons_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizActionButtons(primaryButton: .init(state: .normal, action: {}))

            StepQuizActionButtons(
                retryButton: .init(action: {}),
                primaryButton: .init(state: .normal, action: {})
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
