import shared
import SwiftUI

struct StepQuizActionButtons: View {
    var retryButton: RetryButton?

    var continueButton: ContinueButton?

    var primaryButton: PrimaryButton?

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            if let retryButton {
                StepQuizRetryButton(
                    appearance: retryButton.appearance,
                    style: retryButton.style,
                    onTap: retryButton.action
                )
            }

            if let continueButton {
                StepQuizActionButton(
                    state: .correct,
                    isLoading: continueButton.isLoading,
                    onTap: continueButton.action
                )
            }

            if let primaryButton {
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

        var style: StepQuizRetryButton.Style = .logoOnly

        let action: () -> Void
    }

    struct ContinueButton {
        let isLoading: Bool
        let action: () -> Void
    }

    struct PrimaryButton {
        let state: StepQuizActionButton.State

        var titleForState: ((StepQuizActionButton.State) -> String?)?
        var systemImageNameForState: ((StepQuizActionButton.State) -> String?)?

        let action: () -> Void
    }
}

// MARK: - Convenience Init -

extension StepQuizActionButtons {
    static func submit(state: StepQuizActionButton.State, action: @escaping () -> Void) -> StepQuizActionButtons {
        StepQuizActionButtons(primaryButton: .init(state: state, action: action))
    }

    static func runSolution(state: StepQuizActionButton.State, action: @escaping () -> Void) -> StepQuizActionButtons {
        StepQuizActionButtons(
            primaryButton: .init(
                state: state,
                titleForState: StepQuizActionButtonCodeQuizDelegate.getTitle(for:),
                systemImageNameForState: StepQuizActionButtonCodeQuizDelegate.getSystemImageName(for:),
                action: action
            )
        )
    }

    static func retry(action: @escaping () -> Void) -> StepQuizActionButtons {
        StepQuizActionButtons(retryButton: .init(style: .roundedRectangle, action: action))
    }

    static func `continue`(isLoading: Bool, action: @escaping () -> Void) -> StepQuizActionButtons {
        StepQuizActionButtons(continueButton: .init(isLoading: isLoading, action: action))
    }

    static func retryLogoAndSubmit(
        retryButtonAction: @escaping () -> Void,
        submitButtonState: StepQuizActionButton.State,
        submitButtonAction: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            retryButton: .init(style: .logoOnly, action: retryButtonAction),
            primaryButton: .init(state: submitButtonState, action: submitButtonAction)
        )
    }

    static func retryLogoAndRunSolution(
        retryButtonAction: @escaping () -> Void,
        runSolutionButtonState: StepQuizActionButton.State,
        runSolutionButtonAction: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            retryButton: .init(style: .logoOnly, action: retryButtonAction),
            primaryButton: .init(
                state: runSolutionButtonState,
                titleForState: StepQuizActionButtonCodeQuizDelegate.getTitle(for:),
                systemImageNameForState: StepQuizActionButtonCodeQuizDelegate.getSystemImageName(for:),
                action: runSolutionButtonAction
            )
        )
    }

    static func retryLogoAndContinue(
        retryButtonAction: @escaping () -> Void,
        isContinueButtonLoading: Bool,
        continueButtonAction: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            retryButton: .init(style: .logoOnly, action: retryButtonAction),
            continueButton: .init(isLoading: isContinueButtonLoading, action: continueButtonAction)
        )
    }
}

// MARK: - StepQuizActionButtons_Previews: PreviewProvider -

struct StepQuizActionButtons_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizActionButtons.submit(state: .default, action: {})

            StepQuizActionButtons.runSolution(state: .default, action: {})

            StepQuizActionButtons.retry {}

            StepQuizActionButtons.continue(isLoading: false) {}

            StepQuizActionButtons.retryLogoAndSubmit(
                retryButtonAction: {},
                submitButtonState: .default,
                submitButtonAction: {}
            )

            StepQuizActionButtons.retryLogoAndRunSolution(
                retryButtonAction: {},
                runSolutionButtonState: .default,
                runSolutionButtonAction: {}
            )

            StepQuizActionButtons.retryLogoAndContinue(
                retryButtonAction: {},
                isContinueButtonLoading: false,
                continueButtonAction: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
