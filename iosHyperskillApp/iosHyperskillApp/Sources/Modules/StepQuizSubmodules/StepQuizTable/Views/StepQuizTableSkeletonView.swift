import SwiftUI

struct StepQuizTableSkeletonView: View {
    let itemHeight: CGFloat

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                SkeletonRoundedButton(height: itemHeight)
            }
        }
    }
}

#if DEBUG
#Preview {
    StepQuizTableSkeletonView(itemHeight: 44)
        .padding()
}
#endif
