import Foundation
import shared

enum GamificationToolbarViewActionHandler {
    static func handle(
        viewAction: GamificationToolbarFeatureActionViewAction,
        stackRouter: StackRouterProtocol
    ) {
        switch GamificationToolbarFeatureActionViewActionKs(viewAction) {
        case .showProfileTab:
            TabBarRouter(tab: .profile).route()
        case .showProgressScreen:
            let assembly = ProgressScreenAssembly()
            stackRouter.pushViewController(assembly.makeModule())
        case .showSearchScreen:
            #warning("TODO: ALTAPPS-1058 show search screen")
        }
    }
}
