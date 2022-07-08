import shared
import SwiftUI

final class ProfileAssembly: Assembly {
    private let presentationDescription: ProfilePresentationDescription

    init(presentationDescription: ProfilePresentationDescription) {
        self.presentationDescription = presentationDescription
    }

    convenience init(otherUserID: Int) {
        self.init(presentationDescription: .init(profileType: .otherUser(profileUserID: otherUserID)))
    }

    func makeModule() -> ProfileView {
        let profileComponent = AppGraphBridge.sharedAppGraph.buildProfileComponent()

        let viewModel = ProfileViewModel(
            presentationDescription: presentationDescription,
            feature: profileComponent.profileFeature
        )

        return ProfileView(viewModel: viewModel, viewData: .placeholder)
    }
}
