import shared
import SwiftUI

final class TopicCompletedModalAssembly: UIKitAssembly {
    private weak var moduleOutput: TopicCompletedModalOutputProtocol?

    private let params: TopicCompletedModalFeatureParams

    init(
        params: TopicCompletedModalFeatureParams,
        output: TopicCompletedModalOutputProtocol?
    ) {
        self.params = params
        self.moduleOutput = output
    }

    func makeModule() -> UIViewController {
        let topicCompletedModalComponent =
          AppGraphBridge.sharedAppGraph.buildTopicCompletedModalComponent(params: params)

        let topicCompletedModalViewModel = TopicCompletedModalViewModel(
            feature: topicCompletedModalComponent.topicCompletedModalFeature
        )
        topicCompletedModalViewModel.moduleOutput = moduleOutput

        let topicCompletedModalView = TopicCompletedModalView(
            viewModel: topicCompletedModalViewModel
        )

        let hostingController = TopicCompletedModalHostingController(
            rootView: topicCompletedModalView
        )

        return hostingController
    }
}
