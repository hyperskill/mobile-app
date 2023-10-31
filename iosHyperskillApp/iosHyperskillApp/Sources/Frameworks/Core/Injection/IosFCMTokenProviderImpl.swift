import FirebaseMessaging
import Foundation
import shared

final class IosFCMTokenProviderImpl: IosFCMTokenProvider {
    static let shared = IosFCMTokenProviderImpl()

    func getFCMToken() async throws -> String? {
        guard Messaging.messaging().apnsToken != nil else {
            return nil
        }
        return try await Messaging.messaging().token()
    }
}
