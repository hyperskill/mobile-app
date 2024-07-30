import shared
import SwiftUI

// swiftlint:disable function_parameter_count
extension View {
    @ViewBuilder
    func stepToolbar(
        state: StepFeature.ViewState,
        onCommentsButtonTap: @escaping () -> Void,
        onShareButtonTap: @escaping () -> Void,
        onReportButtonTap: @escaping () -> Void,
        onSkipButtonTap: @escaping () -> Void,
        onOpenInWebButtonTap: @escaping () -> Void
    ) -> some View {
        toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Menu {
                    if state.stepMenuSecondaryActions.contains(.comments) {
                        Button(action: onCommentsButtonTap) {
                            Label("Comments", systemImage: "message")
                        }
                    }

                    if state.stepMenuSecondaryActions.contains(.share) {
                        Button(action: onShareButtonTap) {
                            Label("Share", systemImage: "square.and.arrow.up")
                        }
                    }

                    if state.stepMenuSecondaryActions.contains(.report) {
                        Button(action: onReportButtonTap) {
                            Label("Report", systemImage: "flag")
                        }
                    }

                    if state.stepMenuSecondaryActions.contains(.skip) {
                        Button(action: onSkipButtonTap) {
                            Label("Skip", systemImage: "arrow.right")
                        }
                    }

                    if state.stepMenuSecondaryActions.contains(.openInWeb) {
                        Button(action: onOpenInWebButtonTap) {
                            Label("Open in Web", systemImage: "safari")
                        }
                    }
                } label: {
                    Image(systemName: "ellipsis.circle")
                }
                .disabled(
                    state.stepState is StepFeatureStepStateIdle || state.stepState is StepFeatureStepStateLoading
                )
            }
        }
    }
}
// swiftlint:enable function_parameter_count
