import SwiftUI

extension View {
    @ViewBuilder
    @available(iOS, deprecated: 15.0)
    func listRowSeparatorHidden() -> some View {
        if #available(iOS 15.0, *) {
            self.listRowSeparator(.hidden)
        } else {
            self
        }
    }
}
