import shared
import SwiftUI

final class TopicCompletedModalAssembly: UIKitAssembly {
    private let params: TopicCompletedModalFeatureParams

    init(params: TopicCompletedModalFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let topicCompletedModalComponent =
          AppGraphBridge.sharedAppGraph.buildTopicCompletedModalComponent(params: params)

        let topicCompletedModalViewModel = TopicCompletedModalViewModel(
            feature: topicCompletedModalComponent.topicCompletedModalFeature
        )

        let topicCompletedModalView = TopicCompletedModalView(
            viewModel: topicCompletedModalViewModel
        )

        let hostingController = TopicCompletedModalHostingController(
            rootView: topicCompletedModalView
        )

        return hostingController
    }
}
