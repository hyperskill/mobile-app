import SwiftUI

struct SearchPlaceholderLoadingView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            LazyVStack(alignment: .leading) {
                ForEach(0..<20) { _ in
                    SkeletonRoundedView()
                        .frame(height: 60)
                }
            }
            .padding([.horizontal, .bottom])
        }
        .edgesIgnoringSafeArea(.bottom)
    }
}

#Preview {
    SearchPlaceholderLoadingView()
}
