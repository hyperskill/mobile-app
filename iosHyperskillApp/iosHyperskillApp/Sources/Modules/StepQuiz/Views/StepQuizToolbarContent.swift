import shared
import SwiftUI

extension View {
    @ViewBuilder
    func stepQuizToolbar(
        state: StepQuizFeature.State,
        onLimitsButtonTap: @escaping () -> Void,
        onTheoryButtonTap: @escaping () -> Void
    ) -> some View {
        let isTheoryToolbarItemAvailable = StepQuizResolver.shared.isTheoryToolbarItemAvailable(
            state: state.stepQuizState
        )
        let isQuizLoading = StepQuizResolver.shared.isQuizLoading(state: state.stepQuizState)

        let stepQuizToolbarViewState = StepQuizToolbarViewStateMapper.shared.map(state: state.stepQuizToolbarState)
        let stepsLimitLabel =
            (stepQuizToolbarViewState as? StepQuizToolbarFeatureViewStateContentVisible)?.stepsLimitLabel
        let isLimitsToolbarItemAvailable =
            (stepQuizToolbarViewState as? StepQuizToolbarFeatureViewStateContentVisible) != nil
        let isLimitsToolbarItemDisabled = isQuizLoading || stepsLimitLabel == nil

        if #available(iOS 16.0, *) {
            self.toolbar {
                StepQuizToolbarContent(
                    limitsText: stepsLimitLabel,
                    isLimitsToolbarItemAvailable: isLimitsToolbarItemAvailable,
                    isLimitsToolbarItemDisabled: isLimitsToolbarItemDisabled,
                    isTheoryToolbarItemAvailable: isTheoryToolbarItemAvailable,
                    isTheoryToolbarItemDisabled: isQuizLoading,
                    onLimitsButtonTap: onLimitsButtonTap,
                    onTheoryButtonTap: onTheoryButtonTap
                )
            }
        } else {
            self.if(isLimitsToolbarItemAvailable) {
                $0.toolbar {
                    LimitsToolbarItem(
                        text: stepsLimitLabel,
                        disabled: isLimitsToolbarItemDisabled,
                        onTap: onLimitsButtonTap
                    )
                }
            }
            self.if(isTheoryToolbarItemAvailable) {
                $0.toolbar {
                    TheoryToolbarItem(
                        disabled: isQuizLoading,
                        onTap: onTheoryButtonTap
                    )
                }
            }
        }
    }
}

@available(iOS 16.0, *)
private struct StepQuizToolbarContent: ToolbarContent {
    let limitsText: String?
    let isLimitsToolbarItemAvailable: Bool
    let isLimitsToolbarItemDisabled: Bool

    let isTheoryToolbarItemAvailable: Bool
    let isTheoryToolbarItemDisabled: Bool

    let onLimitsButtonTap: () -> Void
    let onTheoryButtonTap: () -> Void

    var body: some ToolbarContent {
        if isLimitsToolbarItemAvailable {
            LimitsToolbarItem(
                text: limitsText,
                disabled: isLimitsToolbarItemDisabled,
                onTap: onLimitsButtonTap
            )
        } else {
            ToolbarItem(placement: .principal) {
                Text("")
            }
        }

        if isTheoryToolbarItemAvailable {
            TheoryToolbarItem(
                disabled: isTheoryToolbarItemDisabled,
                onTap: onTheoryButtonTap
            )
        }
    }
}

private struct TheoryToolbarItem: ToolbarContent {
    let disabled: Bool
    let onTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .navigationBarTrailing) {
            Button(
                action: onTap,
                label: { Image(systemName: "book") }
            )
            .disabled(disabled)
        }
    }
}

private struct LimitsToolbarItem: ToolbarContent {
    let text: String?
    let disabled: Bool
    let onTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .principal) {
            Button(action: onTap) {
                HStack {
                    Image(.stepQuizToolbarLimits)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxWidth: 24, maxHeight: 24)

                    Text(text)
                        .foregroundColor(.secondaryText)
                        .frame(minWidth: 19)
                }
            }
            .font(.headline)
            .disabled(disabled)
            .conditionalOpacity(isEnabled: !disabled)
        }
    }
}

#if DEBUG
@available(iOS 16.0, *)
#Preview {
    NavigationView {
        Text("Hello, World!")
            .navigationBarHidden(false)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                StepQuizToolbarContent(
                    limitsText: "5",
                    isLimitsToolbarItemAvailable: true,
                    isLimitsToolbarItemDisabled: false,
                    isTheoryToolbarItemAvailable: true,
                    isTheoryToolbarItemDisabled: false,
                    onLimitsButtonTap: {},
                    onTheoryButtonTap: {}
                )
            }
    }
}
#endif
