import shared

class TopicsRepetitionsAssembly: UIKitAssembly {
    private let pushRouter: SwiftUIPushRouter

    init(pushRouter: SwiftUIPushRouter) {
        self.pushRouter = pushRouter
    }

    func makeModule() -> UIViewController {
        let topicsRepetitionsComponent = AppGraphBridge.sharedAppGraph.buildTopicsRepetitionsComponent()

        let viewModel = TopicsRepetitionsViewModel(feature: topicsRepetitionsComponent.topicsRepetitionsFeature)

        let topicsRepetitionsView = TopicsRepetitionsView(
            viewModel: viewModel,
            pushRouter: self.pushRouter,
            dataMapper: topicsRepetitionsComponent.topicsRepetitionsViewDataMapper
        )

        return TopicsRepetitionsHostingController(rootView: topicsRepetitionsView)
    }
}
