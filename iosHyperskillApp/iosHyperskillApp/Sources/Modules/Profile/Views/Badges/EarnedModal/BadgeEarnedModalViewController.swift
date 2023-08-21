import Foundation
import shared
import SwiftUI

protocol BadgeEarnedModalViewControllerDelegate: AnyObject {
    func badgeEarnedModalViewControllerDidAppear(
        _ viewController: BadgeEarnedModalViewController,
        badgeKind: BadgeKind
    )
    func badgeEarnedModalViewControllerDidDisappear(
        _ viewController: BadgeEarnedModalViewController,
        badgeKind: BadgeKind
    )
}

final class BadgeEarnedModalAssembly: UIKitAssembly {
    private let badge: Badge

    private weak var delegate: BadgeEarnedModalViewControllerDelegate?

    init(
        badge: Badge,
        delegate: BadgeEarnedModalViewControllerDelegate?
    ) {
        self.badge = badge
        self.delegate = delegate
    }

    func makeModule() -> UIViewController {
        let badgesViewStateMapper = BadgesViewStateMapper(
            resourceProvider: AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider
        )
        let earnedBadgeModalViewState = badgesViewStateMapper.mapToEarnedBadgeModalViewState(badge: badge)

        return BadgeEarnedModalViewController(
            earnedBadgeModalViewState: earnedBadgeModalViewState,
            delegate: delegate
        )
    }
}

final class BadgeEarnedModalViewController: PanModalSwiftUIViewController<BadgeEarnedModalView> {
    weak var delegate: BadgeEarnedModalViewControllerDelegate?

    private let earnedBadgeModalViewState: EarnedBadgeModalViewState

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(
        earnedBadgeModalViewState: EarnedBadgeModalViewState,
        delegate: BadgeEarnedModalViewControllerDelegate?
    ) {
        self.earnedBadgeModalViewState = earnedBadgeModalViewState
        self.delegate = delegate

        let view = BadgeEarnedModalView(earnedBadgeModalViewState: earnedBadgeModalViewState)

        super.init(isPresented: .constant(false), content: { view })
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.badgeEarnedModalViewControllerDidAppear(self, badgeKind: earnedBadgeModalViewState.kind)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.badgeEarnedModalViewControllerDidDisappear(self, badgeKind: earnedBadgeModalViewState.kind)
    }
}
