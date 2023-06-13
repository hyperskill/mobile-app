import SwiftUI

protocol StreakRecoveryModalViewControllerDelegate: AnyObject {
    func streakRecoveryModalViewControllerDidTapRestoreStreakButton()

    func streakRecoveryModalViewControllerDidTapNoThanksButton()

    func streakRecoveryModalViewControllerDidAppear(_ viewController: StreakRecoveryModalViewController)

    func streakRecoveryModalViewControllerDidDisappear(_ viewController: StreakRecoveryModalViewController)
}

class StreakRecoveryModalViewController: PanModalSwiftUIViewController<StreakRecoveryModalView> {
    weak var delegate: StreakRecoveryModalViewControllerDelegate?

    convenience init(
        recoveryPriceAmount: String,
        recoveryPriceLabel: String,
        modalText: String,
        delegate: StreakRecoveryModalViewControllerDelegate?
    ) {
        self.init(
            isPresented: .init(get: { false }, set: { _ in }),
            content: {
                StreakRecoveryModalView(
                    recoveryPriceAmount: recoveryPriceAmount,
                    recoveryPriceLabel: recoveryPriceLabel,
                    modalText: modalText,
                    restoreStreakButtonTapped: {
                        delegate?.streakRecoveryModalViewControllerDidTapRestoreStreakButton()
                    },
                    noThanksButtonTapped: {
                        delegate?.streakRecoveryModalViewControllerDidTapNoThanksButton()
                    }
                )
            },
            delegate: delegate
        )
    }

    init(
        isPresented: Binding<Bool>,
        content: @escaping () -> StreakRecoveryModalView,
        delegate: StreakRecoveryModalViewControllerDelegate?
    ) {
        self.delegate = delegate
        super.init(isPresented: isPresented, content: content)
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.streakRecoveryModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.streakRecoveryModalViewControllerDidDisappear(self)
    }
}
