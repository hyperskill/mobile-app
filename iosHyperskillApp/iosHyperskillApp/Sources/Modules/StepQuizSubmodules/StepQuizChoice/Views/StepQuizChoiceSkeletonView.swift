import SwiftUI

struct StepQuizChoiceSkeletonView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                SkeletonRoundedButton()
            }

            SkeletonRoundedButton()
                .padding(.top)
        }
    }
}

#if DEBUG
#Preview {
    StepQuizChoiceSkeletonView()
        .padding()
}
#endif
