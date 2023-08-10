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
        let profileComponent = AppGraphBridge.sharedAppGraph.buildProfileComponent()

        let notificationComponent = AppGraphBridge.sharedAppGraph.buildNotificationComponent()

        let viewModel = ProfileViewModel(
            presentationDescription: presentationDescription,
            profileViewDataMapper: ProfileViewDataMapper(),
            badgesViewStateMapper: profileComponent.badgesViewStateMapper,
            notificationService: NotificationsService(
                notificationInteractor: notificationComponent.notificationInteractor
            ),
            notificationsRegistrationService: .shared,
            notificationInteractor: notificationComponent.notificationInteractor,
            feature: profileComponent.profileFeature
        )

        return ProfileView(viewModel: viewModel, panModalPresenter: PanModalPresenter())
    }
}
