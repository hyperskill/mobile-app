import shared
import SwiftUI

struct ProgressScreenProjectProgressView: View {
    let projectProgressViewStateKs: ProgressScreenViewStateProjectProgressViewStateKs

    var body: some View {
        switch projectProgressViewStateKs {
        case .idle, .loading:
            ProgressView()
        case .empty:
            EmptyView()
        case .error:
            EmptyView()
        case .content(let data):
            ProgressScreenProjectBlockView(
                appearance: .init(),
                projectLevel: data.level.flatMap(SharedProjectLevelWrapper.init(sharedProjectLevel:)),
                title: data.title,
                timeToCompleteLabel: data.timeToCompleteLabel,
                completedStagesLabel: data.completedStagesLabel,
                completedStagesProgress: data.completedStagesProgress,
                isCompleted: data.isCompleted
            )
        }
    }
}

//struct ProgressScreenProjectProgressView_Previews: PreviewProvider {
//    static var previews: some View {
//        ProgressScreenProjectProgressView()
//    }
//}
