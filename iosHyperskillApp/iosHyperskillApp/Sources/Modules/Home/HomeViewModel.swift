import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var wasInBackground = false

    override init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: UIApplication.shared
        )
    }

    func loadContent(forceUpdate: Bool = false) {
        onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(HomeFeatureMessageHomeViewedEventMessage())
    }

    private func logClickedProblemOfDayCardEvent() {
        onNewMessage(HomeFeatureMessageHomeClickedProblemOfDayCardEventMessage())
    }

    // MARK: Private API

    @objc
    private func handleApplicationDidBecomeActive() {
        if wasInBackground {
            wasInBackground = false
            onNewMessage(HomeFeatureMessageInit(forceUpdate: true))
        }
    }

    @objc
    private func handleApplicationDidEnterBackground() {
        wasInBackground = true
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        loadContent(forceUpdate: true)
    }

    func handleProblemOfDayContinueActionPerformed() {
        logClickedProblemOfDayCardEvent()
    }
}
