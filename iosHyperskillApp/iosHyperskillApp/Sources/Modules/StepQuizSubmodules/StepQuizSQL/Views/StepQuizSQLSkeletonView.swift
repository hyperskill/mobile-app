import SwiftUI

struct StepQuizSQLSkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 146)
    }
}

#if DEBUG
#Preview {
    StepQuizSQLSkeletonView()
}
#endif
