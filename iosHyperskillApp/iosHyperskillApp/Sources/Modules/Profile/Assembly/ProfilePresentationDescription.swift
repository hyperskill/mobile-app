import Foundation

struct ProfilePresentationDescription {
    let profileType: ProfileType

    enum ProfileType {
        case currentUser
        case otherUser(profileUserID: Int)
    }
}
