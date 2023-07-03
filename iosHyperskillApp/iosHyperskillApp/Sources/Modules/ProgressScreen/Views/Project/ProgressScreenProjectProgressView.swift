import shared
import SwiftUI

extension ProgressScreenProjectProgressView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat
    }
}

struct ProgressScreenProjectProgressView: View {
    let appearance: Appearance

    let projectProgressViewStateKs: ProgressScreenViewStateProjectProgressViewStateKs

    var body: some View {
        switch projectProgressViewStateKs {
        case .idle, .loading:
            ProgressScreenProjectProgressSkeletonView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                )
            )
        case .empty:
            EmptyView()
        case .error:
            EmptyView()
        case .content(let data):
            ProgressScreenProjectProgressContentView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                ),
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
