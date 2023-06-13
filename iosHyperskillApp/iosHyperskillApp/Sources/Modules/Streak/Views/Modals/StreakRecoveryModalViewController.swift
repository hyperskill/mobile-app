import SwiftUI

class StreakRecoveryModalViewController : PanModalSwiftUIViewController<StreakRecoverySheetView> {
    convenience init(content: @escaping () -> StreakRecoverySheetView) {
        self.init(isPresented: .init(get: { false }, set: { _ in }), content: content)
    }
}
