//import shared
//import SwiftUI
//
//extension ProgressScreenView {
//    struct Appearance {
//        let backgroundColor = Color.background
//    }
//}
//
//struct ProgressScreenView: View {
//    private(set) var appearance = Appearance()
//
//    @StateObject var viewModel: ProgressScreenViewModel
//
//    var body: some View {
//        ZStack {
//            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)
//
//            BackgroundView(color: appearance.backgroundColor)
//
//            buildBody()
//        }
//        .onAppear {
//            viewModel.startListening()
//            viewModel.onViewAction = handleViewAction(_:)
//        }
//        .onDisappear {
//            viewModel.stopListening()
//            viewModel.onViewAction = nil
//        }
//    }
//
//    // MARK: Private API
//
//    @ViewBuilder
//    private func buildBody() -> some View {
//        switch viewModel.stateKs {
//        case .idle, .loading:
//            ProgressView()
//        case .networkError:
//            PlaceholderView(
//                configuration: .networkError(
//                    backgroundColor: appearance.backgroundColor,
//                    action: viewModel.doRetryLoadProgressScreen
//                )
//            )
//        case .content(let viewData):
//            Text("Hello, World!")
//        }
//    }
//}
//
//// MARK: - ProgressScreenView (ViewAction) -
//
//private extension ProgressScreenView {
//    func handleViewAction(
//        _ viewAction: ProgressScreenFeatureActionViewAction
//    ) {
//        switch ProgressScreenFeatureActionViewActionKs(viewAction) {}
//    }
//}
//
//// MARK: - ProgressScreenView_Previews: PreviewProvider -
//
//struct ProgressScreenView_Previews: PreviewProvider {
//    static var previews: some View {
//        UIKitViewControllerPreview {
//            ProgressScreenAssembly().makeModule()
//        }
//    }
//}
