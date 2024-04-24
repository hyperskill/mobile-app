import SwiftUI

struct StepQuizStringSkeletonView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            SkeletonRoundedView()
                .frame(height: 96)

            SkeletonRoundedButton()
                .padding(.top)
        }
    }
}

#if DEBUG
#Preview {
    StepQuizStringSkeletonView()
        .padding()
}
#endif
