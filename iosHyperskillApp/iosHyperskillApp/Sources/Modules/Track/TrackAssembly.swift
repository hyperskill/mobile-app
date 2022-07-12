import SwiftUI

final class TrackAssembly: Assembly {
    private let trackID: Int

    init(trackID: Int) {
        self.trackID = trackID
    }

    func makeModule() -> TrackView {
        let trackComponent = AppGraphBridge.sharedAppGraph.buildTrackComponent()

        let viewModel = TrackViewModel(
            trackID: trackID,
            viewDataMapper: TrackViewDataMapper(),
            feature: trackComponent.trackFeature
        )

        return TrackView(viewModel: viewModel)
    }
}
