import shared
import SwiftUI

struct ProgressScreenTrackProgressView: View {
    let trackProgressViewStateKs: ProgressScreenViewStateTrackProgressViewStateKs

    var body: some View {
        switch trackProgressViewStateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            EmptyView()
        case .content(let data):
            ProgressScreenTrackBlockView(
                appearance: .init(),
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
