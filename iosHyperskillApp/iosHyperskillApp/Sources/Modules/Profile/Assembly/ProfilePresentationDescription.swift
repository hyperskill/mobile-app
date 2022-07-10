import Foundation

struct ProfilePresentationDescription {
    let profileType: ProfileType

    enum ProfileType {
        case currentUser
        case otherUser(profileUserID: Int)

        var isCurrentUser: Bool {
            switch self {
            case .currentUser:
                return true
            case .otherUser:
                return false
            }
        }
    }
}
