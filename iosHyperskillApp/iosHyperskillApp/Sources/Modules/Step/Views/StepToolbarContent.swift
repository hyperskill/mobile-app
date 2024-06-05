import shared
import SwiftUI

// swiftlint:disable function_parameter_count
extension View {
    @ViewBuilder
    func stepToolbar(
        stepState: StepFeatureStepState,
        stepMenuActions: Set<StepMenuAction>,
        onShareButtonTap: @escaping () -> Void,
        onReportButtonTap: @escaping () -> Void,
        onSkipButtonTap: @escaping () -> Void,
        onOpenInWebButtonTap: @escaping () -> Void
    ) -> some View {
        toolbar {
            StepToolbarContent(
                disabled: stepState is StepFeatureStepStateIdle || stepState is StepFeatureStepStateLoading,
                stepMenuActions: stepMenuActions,
                onShareButtonTap: onShareButtonTap,
                onReportButtonTap: onReportButtonTap,
                onSkipButtonTap: onSkipButtonTap,
                onOpenInWebButtonTap: onOpenInWebButtonTap
            )
        }
    }
}
// swiftlint:enable function_parameter_count

private struct StepToolbarContent: ToolbarContent {
    let disabled: Bool
    let stepMenuActions: Set<StepMenuAction>

    let onShareButtonTap: () -> Void
    let onReportButtonTap: () -> Void
    let onSkipButtonTap: () -> Void
    let onOpenInWebButtonTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .navigationBarTrailing) {
            Menu {
                if stepMenuActions.contains(.share) {
                    Button(action: onShareButtonTap) {
                        Label("Share", systemImage: "square.and.arrow.up")
                    }
                }

                if stepMenuActions.contains(.report) {
                    Button(action: onReportButtonTap) {
                        Label("Report", systemImage: "flag")
                    }
                }

                if stepMenuActions.contains(.skip) {
                    Button(action: onSkipButtonTap) {
                        Label("Skip", systemImage: "arrow.right")
                    }
                }

                if stepMenuActions.contains(.openInWeb) {
                    Button(action: onOpenInWebButtonTap) {
                        Label("Open in Web", systemImage: "safari")
                    }
                }
            } label: {
                Image(systemName: "ellipsis.circle")
            }
            .disabled(disabled)
        }
    }
}
