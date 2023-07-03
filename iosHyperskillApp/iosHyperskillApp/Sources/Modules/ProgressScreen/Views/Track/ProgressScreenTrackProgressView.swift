import shared
import SwiftUI

extension ProgressScreenTrackProgressView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat
    }
}

struct ProgressScreenTrackProgressView: View {
    let appearance: Appearance

    let trackProgressViewStateKs: ProgressScreenViewStateTrackProgressViewStateKs

    var body: some View {
        switch trackProgressViewStateKs {
        case .idle, .loading:
            ProgressScreenTrackProgressSkeletonView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                )
            )
        case .error:
            EmptyView()
        case .content(let data):
            ProgressScreenTrackProgressContentView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                ),
                avatarImageSource: data.imageSource,
                title: data.title,
                completedTopicsCountLabel: data.completedTopicsCountLabel,
                completedTopicsPercentageLabel: data.completedTopicsPercentageLabel,
                completedTopicsPercentageProgress: data.completedTopicsPercentageProgress,
                appliedTopicsCountLabel: data.appliedTopicsCountLabel,
                appliedTopicsPercentageLabel: data.appliedTopicsPercentageLabel,
                appliedTopicsPercentageProgress: data.appliedTopicsPercentageProgress,
                timeToCompleteLabel: data.timeToCompleteLabel,
                completedGraduateProjectsCount: Int(data.completedGraduateProjectsCount),
                isCompleted: data.isCompleted
            )
        }
    }
}

//struct ProgressScreenTrackProgressView_Previews: PreviewProvider {
//    static var previews: some View {
//        ProgressScreenTrackProgressView()
//    }
//}
