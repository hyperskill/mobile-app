import SwiftUI
import shared

final class FeatureStateStore<State>: ObservableObject {
    private let feature: Presentation_reduxFeature
    private var featureStateListenerCancellable: ModelCancellable?

    @Published var state: State

    init(feature: Presentation_reduxFeature, initialState: State) {
        self.feature = feature
        self.state = initialState
    }

    func addStateListener() {
        self.cancelStateListener()

        self.featureStateListenerCancellable = self.feature.addStateListener { [weak self] newState in
            #if DEBUG
            print("FeatureStateStore :: did receive new state = \(String(describing: newState))")
            #endif

            guard let strongSelf = self,
                  let newState = newState as? State else {
                return
            }

            strongSelf.state = newState
        }
    }

    func cancelStateListener() {
        self.featureStateListenerCancellable?.cancel()
        self.featureStateListenerCancellable = nil
    }
}
