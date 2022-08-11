import Foundation

enum AppPresentingScreen: Identifiable {
    case auth
    case onboarding
    case newUser

    var id: Self { self }
}
