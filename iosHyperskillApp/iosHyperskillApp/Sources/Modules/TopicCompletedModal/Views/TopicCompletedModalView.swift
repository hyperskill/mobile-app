import shared
import SwiftUI

struct TopicCompletedModalView: View {
    @StateObject var viewModel: TopicCompletedModalViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            TopicCompletedModalContentView(
                title: viewModel.state.title,
                description: viewModel.state.description_,
                callToActionButtonTitle: viewModel.state.callToActionButtonTitle,
                spacebotAvatarVariantIndex: Int(viewModel.state.spacebotAvatarVariantIndex),
                backgroundAnimationStyle: viewModel.state.backgroundAnimationStyle,
                onCloseButtonTap: {},
                onCallToActionButtonTap: {}
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
            break
        case .navigateTo:
            break
        }
    }
}
