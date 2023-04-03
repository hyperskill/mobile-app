import SwiftUI

final class StudyPlanAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let studyPlanScreenComponent = AppGraphBridge.sharedAppGraph.buildStudyPlanScreenComponent()

        let viewModel = StudyPlanViewModel(
            feature: studyPlanScreenComponent.studyPlanScreenFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let trackView = StudyPlanView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = StyledHostingController(
            rootView: trackView,
            appearance: .leftAlignedNavigationBarTitle
        )

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
