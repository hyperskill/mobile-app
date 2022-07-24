import SwiftUI

final class TrackAssembly: Assembly {
    private let trackID: Int

    init(trackID: Int) {
        self.trackID = trackID
    }

    func makeModule() -> TrackView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let trackComponent = AppGraphBridge.sharedAppGraph.buildTrackComponent()

        let viewModel = TrackViewModel(
            trackID: trackID,
            viewDataMapper: TrackViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            ),
            feature: trackComponent.trackFeature
        )

        return TrackView(viewModel: viewModel)
    }
}
