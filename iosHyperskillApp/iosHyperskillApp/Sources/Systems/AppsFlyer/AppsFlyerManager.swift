import AppsFlyerLib
import AppTrackingTransparency
import Foundation
import shared

final class AppsFlyerManager {
    static let shared = AppsFlyerManager()

    private init() {}

    func configure() {
        let lib = AppsFlyerLib.shared()

        lib.appsFlyerDevKey = BuildKonfig.companion.APPS_FLYER_DEV_KEY
        lib.appleAppID = AppsFlyerInfo.appleAppID
        lib.waitForATTUserAuthorization(timeoutInterval: 60)
        #if DEBUG
        lib.isDebug = true
        #endif
    }

    func start() {
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
        }
    }
}
