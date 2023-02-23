import SwiftUI
import UIKit

extension StyledHostingController {
    struct Appearance {
        var navigationBar = NavigationBar()

        static var withoutBackButtonTitle: Appearance {
            Appearance(navigationBar: .init(isBackButtonTitleHidden: true))
        }

        static var leftAlignedNavigationBarTitle: Appearance {
            Appearance(navigationBar: .init(titlePosition: .left))
        }

        struct NavigationBar {
            var isBackButtonTitleHidden = false
            var titlePosition = TitlePosition.default

            enum TitlePosition {
                case left
                case `default`

                fileprivate var titlePositionAdjustment: UIOffset {
                    switch self {
                    case .left:
                        return UIOffset(horizontal: -CGFloat.greatestFiniteMagnitude, vertical: 0)
                    case .default:
                        return .zero
                    }
                }
            }
        }
    }
}

final class StyledHostingController<RootView: View>: UIHostingController<RootView>, UINavigationControllerDelegate {
    private let appearance: Appearance

    init(rootView: RootView, appearance: Appearance = Appearance()) {
        self.appearance = appearance
        super.init(rootView: rootView)
    }

    @MainActor
    @available(*, unavailable)
    dynamic required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        if appearance.navigationBar.isBackButtonTitleHidden {
            navigationController?.removeBackButtonTitleForTopController()
        }
        if appearance.navigationBar.titlePosition != .default {
            assert(navigationController?.delegate == nil)
            assert((navigationController?.viewControllers.count ?? 0) <= 1)
            navigationController?.delegate = self
        }
    }

    func navigationController(
        _ navigationController: UINavigationController,
        willShow viewController: UIViewController,
        animated: Bool
    ) {
        guard appearance.navigationBar.titlePosition != .default else {
            return
        }

        let defaultTitlePositionAdjustment = Appearance.NavigationBar.TitlePosition.default.titlePositionAdjustment
        let targetTitlePositionAdjustment = viewController === self
          ? appearance.navigationBar.titlePosition.titlePositionAdjustment
          : defaultTitlePositionAdjustment

        if animated,
           let transitionCoordinator = navigationController.transitionCoordinator {
            transitionCoordinator.animate(
                alongsideTransition: { [weak self] _ in
                    guard let strongSelf = self else {
                        return
                    }

                    strongSelf.changeNavigationBarTitlePositionAdjustment(targetTitlePositionAdjustment)
                },
                completion: { [weak self] context in
                    guard let strongSelf = self else {
                        return
                    }

                    // Rollback appearance
                    if context.isCancelled {
                        strongSelf.changeNavigationBarTitlePositionAdjustment(defaultTitlePositionAdjustment)
                    }
                }
            )
        } else {
            changeNavigationBarTitlePositionAdjustment(targetTitlePositionAdjustment)
        }
    }

    private func changeNavigationBarTitlePositionAdjustment(_ titlePositionAdjustment: UIOffset) {
        navigationController?.navigationBar.standardAppearance.titlePositionAdjustment = titlePositionAdjustment
        navigationController?.navigationBar.scrollEdgeAppearance?.titlePositionAdjustment = titlePositionAdjustment
        navigationController?.navigationBar.compactAppearance?.titlePositionAdjustment = titlePositionAdjustment
    }
}
