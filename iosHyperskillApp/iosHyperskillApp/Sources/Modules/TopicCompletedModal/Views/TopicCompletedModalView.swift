import shared
import SwiftUI

struct TopicCompletedModalView: View {
    @StateObject var viewModel: TopicCompletedModalViewModel

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: viewModel.logShownEvent,
                onViewWillDisappear: viewModel.logHiddenEvent
            )

            TopicCompletedModalContentView(
                title: viewModel.state.title,
                description: viewModel.state.description_,
                earnedGemsText: viewModel.state.earnedGemsText,
                callToActionButtonTitle: viewModel.state.callToActionButtonTitle,
                spacebotAvatarVariantIndex: Int(viewModel.state.spacebotAvatarVariantIndex),
                backgroundAnimationStyle: viewModel.state.backgroundAnimationStyle,
                onCloseButtonTap: viewModel.doCloseAction,
                onCallToActionButtonTap: viewModel.doCallToAction
            )
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }
}

// MARK: - TopicCompletedModalView (ViewAction) -

private extension TopicCompletedModalView {
    func handleViewAction(
        _ viewAction: TopicCompletedModalFeatureActionViewAction
    ) {
        switch TopicCompletedModalFeatureActionViewActionKs(viewAction) {
        case .dismiss:
            dismiss()
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                TopicCompletedModalFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        }
    }

    func handleNavigateToViewAction(_ viewAction: TopicCompletedModalFeatureActionViewActionNavigateToKs) {
        switch viewAction {
        case .nextTopic:
            viewModel.doNextTopicPresentation()
        case .studyPlan:
            viewModel.doStudyPlanPresentation()
        case .paywall(_):
            #warning("TODO ALTAPPS-1309")
        }

        dismiss()
    }

    func dismiss() {
        DispatchQueue.main.async {
            presentationMode.wrappedValue.dismiss()
        }
    }
}
