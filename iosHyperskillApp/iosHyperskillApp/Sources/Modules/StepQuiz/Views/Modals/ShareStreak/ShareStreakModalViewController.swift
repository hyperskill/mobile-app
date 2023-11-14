import Foundation
import shared
import SwiftUI

protocol ShareStreakModalViewControllerDelegate: AnyObject {
    func shareStreakModalViewControllerDidAppear(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    )
    func shareStreakModalViewControllerDidDisappear(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    )

    func shareStreakModalViewControllerDidTapShareButton(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    )
    func shareStreakModalViewControllerDidTapNoThanksButton(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    )
}

final class ShareStreakModalViewController: PanModalSwiftUIViewController<ShareStreakModalView> {
    weak var delegate: ShareStreakModalViewControllerDelegate?

    private let streak: Int

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(streak: Int, delegate: ShareStreakModalViewControllerDelegate?) {
        self.streak = streak
        self.delegate = delegate

        var view = ShareStreakModalView(streak: streak)

        super.init(
            isPresented: .constant(false),
            content: { view }
        )

        view.onShareButtonTap = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.shareStreakModalViewControllerDidTapShareButton(strongSelf, streak: strongSelf.streak)
        }
        view.onNoThanksButtonTap = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.shareStreakModalViewControllerDidTapNoThanksButton(
                strongSelf,
                streak: strongSelf.streak
            )
            strongSelf.dismiss(animated: true)
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.shareStreakModalViewControllerDidAppear(self, streak: streak)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.shareStreakModalViewControllerDidDisappear(self, streak: streak)
    }
}
