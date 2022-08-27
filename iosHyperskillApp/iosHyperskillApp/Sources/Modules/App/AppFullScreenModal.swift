import Foundation

enum AppFullScreenModal: Identifiable {
    case auth
    case onboarding
    case newUser

    var id: Self { self }
}
