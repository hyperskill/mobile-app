import Foundation

protocol AuthOutputProtocol: AnyObject {
    func handleUserAuthorized(isNewUser: Bool)
}
