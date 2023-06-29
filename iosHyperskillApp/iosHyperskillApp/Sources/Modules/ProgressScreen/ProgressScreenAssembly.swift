//import shared
//import SwiftUI
//
//final class ProgressScreenAssembly: UIKitAssembly {
//    func makeModule() -> UIViewController {
//        let progressScreenComponent = AppGraphBridge.sharedAppGraph.buildProgressScreenComponent()
//
//        let progressScreenViewModel = ProgressScreenViewModel(
//            feature: progressScreenComponent.progressScreenFeature
//        )
//
//        let progressScreenView = ProgressScreenView(
//            viewModel: progressScreenViewModel
//        )
//
//        let hostingController = StyledHostingController(
//            rootView: progressScreenView
//        )
//        hostingController.navigationItem.largeTitleDisplayMode = .never
//
//        return hostingController
//    }
//}
