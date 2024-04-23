import Foundation
import shared

enum GamificationToolbarViewActionHandler {
    static func handle(
        viewAction: GamificationToolbarFeatureActionViewAction,
        stackRouter: StackRouterProtocol,
        panModalPresenter: PanModalPresenter
    ) {
        switch GamificationToolbarFeatureActionViewActionKs(viewAction) {
        case .showProfileTab:
            TabBarRouter(tab: .profile).route()
        case .showProgressScreen:
            let assembly = ProgressScreenAssembly()
            stackRouter.pushViewController(assembly.makeModule())
        case .showSearchScreen:
            if #available(iOS 15.0, *) {
                let assembly = SearchAssembly()
                stackRouter.pushViewController(assembly.makeModule())
            }
        case .showProblemsLimitInfoModal(let data):
            let assembly = ProblemsLimitInfoModalAssembly(
                params: ProblemsLimitInfoModalFeatureParams(
                    subscription: data.subscription,
                    chargeLimitsStrategy: data.chargeLimitsStrategy,
                    context: data.context
                )
            )
            panModalPresenter.presentIfPanModal(assembly.makeModule())
        }
    }
}
