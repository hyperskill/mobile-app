import SwiftUI

final class TrackAssembly: Assembly {
    func makeModule() -> TrackView {
        TrackView(viewData: .placeholder)
    }
}
