import AppsFlyerLib
import AppTrackingTransparency
import Foundation
import shared

final class AppsFlyerManager: AppsFlyerAnalyticEngine {
    private static let waitForATTUserAuthorizationTimeoutInterval: TimeInterval = 60

    static let shared = AppsFlyerManager()

    static func configure() {
        let lib = AppsFlyerLib.shared()

        lib.appsFlyerDevKey = BuildKonfig.companion.APPS_FLYER_DEV_KEY
        lib.appleAppID = AppsFlyerInfo.appleAppID
        lib.waitForATTUserAuthorization(timeoutInterval: waitForATTUserAuthorizationTimeoutInterval)

        #if DEBUG
        lib.isDebug = true
        #endif
    }

    static func start() {
        #if DEBUG
        AppsFlyerLib.shared().start { dictionary, error in
            if let error {
                print("AppsFlyerManager: start error = \(error)")
            } else {
                print("AppsFlyerManager: start success = \(String(describing: dictionary))")
            }
        }
        #else
        AppsFlyerLib.shared().start()
        #endif

        ATTrackingManager.requestTrackingAuthorization { authorizationStatus in
            #if DEBUG
            switch authorizationStatus {
            case .notDetermined:
                print("AppsFlyerManager: application cannot determine the authorisation status for access")
            case .restricted:
                print("AppsFlyerManager: display permission not shown to the user and also tracking restricted")
            case .denied:
                print("AppsFlyerManager: user has not authorized the application")
            case .authorized:
                print("AppsFlyerManager: user has authorized the application")
            @unknown default:
                print("AppsFlyerManager: @unknown default")
            }
            #endif

            NotificationCenter.default.post(
                name: .attAuthorizationStatusDidChange,
                object: authorizationStatus
            )
        }
    }

    // MARK: AnalyticEngine

    override func reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Bool,
        completionHandler: @escaping (Error?) -> Void
    ) {
        completionHandler(nil)

        let name = event.name
        let values = event.params

        AppsFlyerLib.shared().customerUserID = userProperties.userId?.stringValue

        #if DEBUG
        print("AppsFlyerManager: logging event = \(name) with values = \(values)")
        AppsFlyerLib.shared().logEvent(
            name: name,
            values: values,
            completionHandler: { dictionaryOrNil, errorOrNil in
                if let error = errorOrNil {
                    assertionFailure("AppsFlyerManager: failed log event = \(name) with error =\(error)")
                } else {
                    print("""
AppsFlyerManager: successfully logged event = \(name) with result = \(String(describing: dictionaryOrNil))
""")
                }
            }
        )
        #else
        AppsFlyerLib.shared().logEvent(name: name, values: values)
        #endif
    }
}

// MARK: - NotificationCenter -

extension Foundation.Notification.Name {
    static let attAuthorizationStatusDidChange = Foundation.Notification.Name("attAuthorizationStatusDidChange")
}
