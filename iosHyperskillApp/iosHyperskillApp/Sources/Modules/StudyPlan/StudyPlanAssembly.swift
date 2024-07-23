import SwiftUI

final class StudyPlanAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let studyPlanScreenComponent = AppGraphBridge.sharedAppGraph.buildStudyPlanScreenComponent()

        let viewModel = StudyPlanViewModel(
            feature: studyPlanScreenComponent.studyPlanScreenFeature
        )

        let stackRouter = StackRouter()
        let modalRouter = ModalRouter()
        let panModalPresenter = PanModalPresenter()

        let trackView = StudyPlanView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: panModalPresenter
        )
        let hostingController = StyledHostingController(
            rootView: trackView,
            appearance: .leftAlignedNavigationBarTitle
        )

        stackRouter.rootViewController = hostingController
        modalRouter.rootViewController = hostingController
        panModalPresenter.rootViewController = hostingController

        return hostingController
    }
}
