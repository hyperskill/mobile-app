import Foundation
import shared

protocol AuthOutputProtocol: AnyObject {
    func handleUserAuthorized(profile: Profile)
}
