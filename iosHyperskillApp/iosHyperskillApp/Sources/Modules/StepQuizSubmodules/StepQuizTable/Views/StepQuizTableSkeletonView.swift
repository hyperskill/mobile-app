import SwiftUI

struct StepQuizTableSkeletonView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                SkeletonRoundedButton()
            }
        }
    }
}

#if DEBUG
#Preview {
    StepQuizTableSkeletonView()
        .padding()
}
#endif
