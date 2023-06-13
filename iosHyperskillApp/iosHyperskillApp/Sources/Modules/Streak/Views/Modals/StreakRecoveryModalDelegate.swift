import Foundation

protocol StreakRecoveryModalDelegate: AnyObject {
    func streakRecoveryModalDidTapRestoreStreakButton()

    func streakRecoveryModalDidTapNoThanksButton()

    func streakRecoveryModalDidAppear()

    func streakRecoveryModalDidDisappear()
}
