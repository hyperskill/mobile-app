import SwiftUI

final class ProfileAssembly: Assembly {
    func makeModule() -> ProfileView {
        ProfileView(viewData: .placeholder)
    }
}
