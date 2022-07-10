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

    static func currentUser() -> ProfileAssembly {
        ProfileAssembly(presentationDescription: .init(profileType: .currentUser))
    }

    func makeModule() -> ProfileView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let profileComponent = AppGraphBridge.sharedAppGraph.buildProfileComponent()

        let viewModel = ProfileViewModel(
            presentationDescription: presentationDescription,
            viewDataMapper: ProfileViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            ),
            feature: profileComponent.profileFeature
        )

        return ProfileView(viewModel: viewModel)
    }
}
