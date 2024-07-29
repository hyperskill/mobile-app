import shared
import SwiftUI

// swiftlint:disable function_parameter_count
extension View {
    @ViewBuilder
    func stepToolbar(
        state: StepFeature.ViewState,
        onCommentButtonTap: @escaping () -> Void,
        onShareButtonTap: @escaping () -> Void,
        onReportButtonTap: @escaping () -> Void,
        onSkipButtonTap: @escaping () -> Void,
        onOpenInWebButtonTap: @escaping () -> Void
    ) -> some View {
        let isMenuActionsDisabled =
            state.stepState is StepFeatureStepStateIdle || state.stepState is StepFeatureStepStateLoading

        if #available(iOS 16.0, *) {
            toolbar {
                StepToolbarContent(
                    isCommentsToolbarItemAvailable: false,
                    menuActions: state.stepMenuSecondaryActions,
                    isMenuActionsDisabled: isMenuActionsDisabled,
                    onCommentButtonTap: onCommentButtonTap,
                    onShareButtonTap: onShareButtonTap,
                    onReportButtonTap: onReportButtonTap,
                    onSkipButtonTap: onSkipButtonTap,
                    onOpenInWebButtonTap: onOpenInWebButtonTap
                )
            }
        } else {
            toolbar {
                StepMenuActionsToolbarItem(
                    actions: state.stepMenuSecondaryActions,
                    disabled: isMenuActionsDisabled,
                    onShareButtonTap: onShareButtonTap,
                    onReportButtonTap: onReportButtonTap,
                    onSkipButtonTap: onSkipButtonTap,
                    onOpenInWebButtonTap: onOpenInWebButtonTap
                )
            }
        }
    }
}
// swiftlint:enable function_parameter_count

@available(iOS 16.0, *)
private struct StepToolbarContent: ToolbarContent {
    #warning("TOOD: Replace with stepMenuSecondaryActions in ALTAPPS-1314")
    let isCommentsToolbarItemAvailable: Bool

    let menuActions: Set<StepMenuSecondaryAction>
    let isMenuActionsDisabled: Bool

    let onCommentButtonTap: () -> Void

    let onShareButtonTap: () -> Void
    let onReportButtonTap: () -> Void
    let onSkipButtonTap: () -> Void
    let onOpenInWebButtonTap: () -> Void

    var body: some ToolbarContent {
        if isCommentsToolbarItemAvailable {
            CommentsToolbarItem(action: onCommentButtonTap)
        }

        StepMenuActionsToolbarItem(
            actions: menuActions,
            disabled: isMenuActionsDisabled,
            onShareButtonTap: onShareButtonTap,
            onReportButtonTap: onReportButtonTap,
            onSkipButtonTap: onSkipButtonTap,
            onOpenInWebButtonTap: onOpenInWebButtonTap
        )
    }
}

private struct CommentsToolbarItem: ToolbarContent {
    let action: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .automatic) {
            Button(action: action) {
                Image(systemName: "message")
            }
        }
    }
}

private struct StepMenuActionsToolbarItem: ToolbarContent {
    let actions: Set<StepMenuSecondaryAction>
    let disabled: Bool

    let onShareButtonTap: () -> Void
    let onReportButtonTap: () -> Void
    let onSkipButtonTap: () -> Void
    let onOpenInWebButtonTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .navigationBarTrailing) {
            Menu {
                if actions.contains(.share) {
                    Button(action: onShareButtonTap) {
                        Label("Share", systemImage: "square.and.arrow.up")
                    }
                }

                if actions.contains(.report) {
                    Button(action: onReportButtonTap) {
                        Label("Report", systemImage: "flag")
                    }
                }

                if actions.contains(.skip) {
                    Button(action: onSkipButtonTap) {
                        Label("Skip", systemImage: "arrow.right")
                    }
                }

                if actions.contains(.openInWeb) {
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
