import SwiftUI

struct ProblemOfDaySkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 200)
    }
}

struct ProblemOfDaySkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemOfDaySkeletonView()
    }
}
