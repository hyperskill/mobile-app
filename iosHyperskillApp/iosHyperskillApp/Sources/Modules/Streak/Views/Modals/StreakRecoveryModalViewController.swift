import SwiftUI

class StreakRecoveryModalViewController: PanModalSwiftUIViewController<StreakRecoveryModalView> {
    convenience init(content: StreakRecoveryModalView) {
        self.init(isPresented: .init(get: { false }, set: { _ in }), content: { content })
    }
}
