import Foundation
import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var wasInBackground = false

    override init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive(_:)),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidEnterBackground(_:)),
            name: UIApplication.didEnterBackgroundNotification,
            object: UIApplication.shared
        )
    }

    func loadContent(forceUpdate: Bool = false) {
        onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func logViewedEvent() {
        onNewMessage(HomeFeatureMessageHomeViewedEventMessage())
    }

    // MARK: Private API

    @objc
    private func handleApplicationDidBecomeActive(_: NSNotification) {
        if wasInBackground {
            wasInBackground = false
            onNewMessage(HomeFeatureMessageInit(forceUpdate: true))
        }
    }

    @objc
    private func handleApplicationDidEnterBackground(_: NSNotification) {
        wasInBackground = true
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        loadContent(forceUpdate: true)
    }
}
