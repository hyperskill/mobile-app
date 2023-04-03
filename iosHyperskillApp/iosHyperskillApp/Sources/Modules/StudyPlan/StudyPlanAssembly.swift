import SwiftUI

final class StudyPlanAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let studyPlanScreenComponent = AppGraphBridge.sharedAppGraph.buildStudyPlanScreenComponent()

        let viewModel = StudyPlanViewModel(
            feature: studyPlanScreenComponent.studyPlanScreenFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let trackView = StudyPlanView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = UIHostingController(rootView: trackView)

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
