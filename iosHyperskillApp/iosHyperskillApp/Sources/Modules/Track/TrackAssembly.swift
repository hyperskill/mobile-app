import SwiftUI

final class TrackAssembly: Assembly {
    func makeModule() -> TrackView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let trackComponent = AppGraphBridge.sharedAppGraph.buildTrackComponent()

        let viewModel = TrackViewModel(
            viewDataMapper: TrackViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            ),
            feature: trackComponent.trackFeature
        )

        return TrackView(viewModel: viewModel)
    }
}
