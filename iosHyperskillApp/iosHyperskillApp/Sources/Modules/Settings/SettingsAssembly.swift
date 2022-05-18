import SwiftUI

final class SettingsAssembly: Assembly {
    func makeModule() -> SettingsView {
        SettingsView()
    }
}
