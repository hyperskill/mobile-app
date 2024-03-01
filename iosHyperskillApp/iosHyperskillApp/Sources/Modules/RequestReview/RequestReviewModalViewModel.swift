import Combine
import Foundation
import shared

final class RequestReviewModalViewModel: FeatureViewModel<
  RequestReviewModalFeature.ViewState,
  RequestReviewModalFeatureMessage,
  RequestReviewModalFeatureActionViewAction
> {
    weak var viewController: RequestReviewModalViewController?

    private var isFirstStateDidChange = true
    private var objectWillChangeSubscription: AnyCancellable?

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        self.objectWillChangeSubscription = objectWillChange.sink { [weak self] _ in
            self?.mainScheduler.schedule { [weak self] in
                if let strongSelf = self {
                    strongSelf.viewController?.displayState(strongSelf.state)
                }
            }
        }
        self.onViewAction = { [weak self] viewAction in
            self?.mainScheduler.schedule { [weak self] in
                if let strongSelf = self {
                    strongSelf.viewController?.displayViewAction(
                        RequestReviewModalFeatureActionViewActionKs(viewAction)
                    )
                }
            }
        }
    }

    override func shouldNotifyStateDidChange(
        oldState: RequestReviewModalFeature.ViewState,
        newState: RequestReviewModalFeature.ViewState
    ) -> Bool {
        if isFirstStateDidChange {
            isFirstStateDidChange = false
            return true
        } else {
            return !oldState.isEqual(newState)
        }
    }

    func doPositiveButtonAction() {
        onNewMessage(RequestReviewModalFeatureMessagePositiveButtonClicked())
    }

    func doNegativeButtonAction() {
        onNewMessage(RequestReviewModalFeatureMessageNegativeButtonClicked())
    }

    func logShownEvent() {
        onNewMessage(RequestReviewModalFeatureMessageShownEventMessage())
    }

    func logHiddenEvent() {
        onNewMessage(RequestReviewModalFeatureMessageHiddenEventMessage())
    }
}
