import SwiftUI

extension View {
    func supportedInterfaceOrientations(_ supportedInterfaceOrientations: UIInterfaceOrientationMask) -> some View {
        onAppear {
            InterfaceOrientationChangesPublisher
                .publishSupportedInterfaceOrientationsDidChange(to: supportedInterfaceOrientations)
        }
        .onDisappear {
            InterfaceOrientationChangesPublisher.publishSupportedInterfaceOrientationsResetToDefault()
        }
    }
}
