// swiftlint:disable:next file_header
//import shared
//import SwiftUI
//
//final class AuthNewUserPlaceholderAssembly: Assembly {
//    private weak var moduleOutput: AuthNewUserPlaceholderOutputProtocol?
//
//    init(output: AuthNewUserPlaceholderOutputProtocol? = nil) {
//        self.moduleOutput = output
//    }
//
//    func makeModule() -> AuthNewUserPlaceholderView {
//        let placeholderNewUserComponent = AppGraphBridge.sharedAppGraph.buildPlaceholderNewUserComponent()
//
//        let viewModel = AuthNewUserPlaceholderViewModel(
//            feature: placeholderNewUserComponent.placeholderNewUserFeature
//        )
//        viewModel.moduleOutput = moduleOutput
//
//        return AuthNewUserPlaceholderView(
//            viewModel: viewModel,
//            viewDataMapper: placeholderNewUserComponent.placeHolderNewUserViewDataMapper
//        )
//    }
//}
