import SwiftUI

/// Custom vertical scroll view with centered content vertically
struct VerticalCenteredScrollView<Content>: View where Content: View {
    var showsIndicators = true

    @ViewBuilder let content: Content

    var body: some View {
        GeometryReader { geometry in
            ScrollView(.vertical, showsIndicators: showsIndicators) {
                content
                    .frame(width: geometry.size.width)
                    .frame(minHeight: geometry.size.height)
            }
            .scrollBounceBehaviorBasedOnSize()
        }
    }
}

struct VerticalCenteredScrollView_Previews: PreviewProvider {
    static var previews: some View {
        VerticalCenteredScrollView {
            VStack {
                Text("1")
                Text("2")
                Text("3")
            }
        }
    }
}
