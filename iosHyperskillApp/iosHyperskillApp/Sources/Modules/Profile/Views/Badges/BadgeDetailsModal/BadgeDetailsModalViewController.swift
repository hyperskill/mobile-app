import Foundation
import shared
import SwiftUI

protocol BadgeDetailsModalViewControllerDelegate: AnyObject {
    func badgeDetailsModalViewControllerDidAppear(
        _ viewController: BadgeDetailsModalViewController,
        badgeKind: BadgeKind
    )
    func badgeDetailsModalViewControllerDidDisappear(
        _ viewController: BadgeDetailsModalViewController,
        badgeKind: BadgeKind
    )
}

final class BadgeDetailsModalAssembly: UIKitAssembly {
    private let badgeDetails: ProfileFeatureActionViewActionBadgeDetails

    private weak var delegate: BadgeDetailsModalViewControllerDelegate?

    init(
        badgeDetails: ProfileFeatureActionViewActionBadgeDetails,
        delegate: BadgeDetailsModalViewControllerDelegate?
    ) {
        self.badgeDetails = badgeDetails
        self.delegate = delegate
    }

    func makeModule() -> UIViewController {
        let badgesViewStateMapper = BadgesViewStateMapper(
            resourceProvider: AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider
        )
        let badgeDetailsViewState = badgesViewStateMapper.map(badgeDetails: badgeDetails)

        return BadgeDetailsModalViewController(
            badgeDetailsViewState: badgeDetailsViewState,
            delegate: delegate
        )
    }
}

final class BadgeDetailsModalViewController: PanModalSwiftUIViewController<BadgeDetailsModalView> {
    weak var delegate: BadgeDetailsModalViewControllerDelegate?

    private let badgeDetailsViewState: BadgeDetailsViewState

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(
        badgeDetailsViewState: BadgeDetailsViewState,
        delegate: BadgeDetailsModalViewControllerDelegate?
    ) {
        self.badgeDetailsViewState = badgeDetailsViewState
        self.delegate = delegate

        let view = BadgeDetailsModalView(badgeDetailsViewState: badgeDetailsViewState)

        super.init(isPresented: .constant(false), content: { view })
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.badgeDetailsModalViewControllerDidAppear(self, badgeKind: badgeDetailsViewState.kind)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.badgeDetailsModalViewControllerDidDisappear(self, badgeKind: badgeDetailsViewState.kind)
    }
}
