import shared
import SwiftUI

struct StepQuizActionButtons: View {
    var retryButton: RetryButton?

    var continueButton: ContinueButton?

    let primaryButton: PrimaryButton

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            if let retryButton = retryButton {
                StepQuizRetryButton(
                    appearance: retryButton.appearance,
                    onTap: retryButton.action
                )
            }

            if let continueButton = continueButton {
                StepQuizActionButton(
                    state: .correct,
                    onTap: continueButton.action
                )
            } else {
                StepQuizActionButton(
                    state: primaryButton.state,
                    titleForState: primaryButton.titleForState,
                    systemImageNameForState: primaryButton.systemImageNameForState,
                    onTap: primaryButton.action
                )
            }
        }
    }

    struct RetryButton {
        var appearance = StepQuizRetryButton.Appearance()

        let action: () -> Void
    }

    struct ContinueButton {
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
            StepQuizActionButtons(
                primaryButton: .init(state: .normal, action: {})
            )

            StepQuizActionButtons(
                retryButton: .init(action: {}),
                continueButton: .init(action: {}),
                primaryButton: .init(state: .normal, action: {})
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
