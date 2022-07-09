import Foundation

struct ProfilePresentationDescription {
    let profileType: ProfileType

    enum ProfileType {
        case currentUser
        case otherUser(profileUserID: Int)

        var isCurrentUser: Bool {
            if case .currentUser = self {
                return true
            }
            return false
        }
    }
}
