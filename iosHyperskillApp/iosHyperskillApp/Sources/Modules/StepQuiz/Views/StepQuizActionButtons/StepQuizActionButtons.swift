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
                    isBounceEffectActive: retryButton.isBounceEffectActive,
                    onTap: retryButton.action
                )
            }

            if let continueButton {
                StepQuizActionButton(
                    state: continueButton.isLoading ? .correctLoading : .correct,
                    isShineEffectActive: false,
                    isPulseEffectActive: false,
                    onTap: continueButton.action
                )
            }

            if let primaryButton {
                StepQuizActionButton(
                    state: primaryButton.state,
                    titleForState: primaryButton.titleForState,
                    systemImageNameForState: primaryButton.systemImageNameForState,
                    isShineEffectActive: primaryButton.isShineEffectActive,
                    isPulseEffectActive: primaryButton.isPulseEffectActive,
                    onTap: primaryButton.action
                )
            }
        }
    }

    struct RetryButton {
        var appearance = StepQuizRetryButton.Appearance()

        var style: StepQuizRetryButton.Style = .logoOnly

        var isBounceEffectActive = false

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

        var isShineEffectActive = false
        var isPulseEffectActive = false

        let action: () -> Void
    }
}

// MARK: - Convenience Init -

extension StepQuizActionButtons {
    static func submit(
        state: StepQuizActionButton.State,
        isShineEffectActive: Bool = false,
        isPulseEffectActive: Bool = false,
        action: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            primaryButton: .init(
                state: state,
                isShineEffectActive: isShineEffectActive,
                isPulseEffectActive: isPulseEffectActive,
                action: action
            )
        )
    }

    static func runSolution(
        state: StepQuizActionButton.State,
        isShineEffectActive: Bool,
        isPulseEffectActive: Bool,
        action: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            primaryButton: .init(
                state: state,
                titleForState: StepQuizActionButtonCodeQuizDelegate.getTitle(for:),
                systemImageNameForState: StepQuizActionButtonCodeQuizDelegate.getSystemImageName(for:),
                isShineEffectActive: isShineEffectActive,
                isPulseEffectActive: isPulseEffectActive,
                action: action
            )
        )
    }

    static func retry(
        isBounceEffectActive: Bool,
        action: @escaping () -> Void
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            retryButton: .init(
                style: .roundedRectangle,
                isBounceEffectActive: isBounceEffectActive,
                action: action
            )
        )
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
        continueButton: ContinueButton
    ) -> StepQuizActionButtons {
        StepQuizActionButtons(
            retryButton: .init(style: .logoOnly, action: retryButtonAction),
            continueButton: continueButton
        )
    }
}

// MARK: - StepQuizActionButtons_Previews: PreviewProvider -

struct StepQuizActionButtons_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            StepQuizActionButtons.submit(
                state: .default,
                isShineEffectActive: false,
                isPulseEffectActive: false,
                action: {}
            )

            StepQuizActionButtons.runSolution(
                state: .default,
                isShineEffectActive: true,
                isPulseEffectActive: true,
                action: {}
            )

            StepQuizActionButtons.retry(isBounceEffectActive: false) {}

            StepQuizActionButtons.continue(isLoading: false) {}
            StepQuizActionButtons.continue(isLoading: true) {}

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
                continueButton: .init(isLoading: false, action: {})
            )
            StepQuizActionButtons.retryLogoAndContinue(
                retryButtonAction: {},
                continueButton: .init(isLoading: true, action: {})
            )
            StepQuizActionButtons.retryLogoAndContinue(
                retryButtonAction: {},
                continueButton: .init(isLoading: true, action: {})
            )
            .disabled(true)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
