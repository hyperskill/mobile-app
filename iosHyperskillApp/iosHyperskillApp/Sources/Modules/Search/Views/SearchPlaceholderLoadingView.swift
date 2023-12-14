import SwiftUI

struct SearchPlaceholderLoadingView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(alignment: .leading) {
                ForEach(0..<10) { _ in
                    SkeletonRoundedView()
                        .frame(height: 60)
                }
            }
            .padding([.horizontal, .bottom])
        }
    }
}

#Preview {
    SearchPlaceholderLoadingView()
}
