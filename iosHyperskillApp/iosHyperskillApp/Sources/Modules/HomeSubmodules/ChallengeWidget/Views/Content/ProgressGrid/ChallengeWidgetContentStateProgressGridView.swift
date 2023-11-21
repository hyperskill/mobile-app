import shared
import SwiftUI

extension ChallengeWidgetContentStateProgressGridView {
    enum Appearance {
        static let columnsCount = 7
    }
}

struct ChallengeWidgetContentStateProgressGridView: View {
    let progressStatuses: [ChallengeWidgetViewStateContentHappeningNow.ProgressStatus]

    var body: some View {
        if progressStatuses.isEmpty {
            EmptyView()
        } else {
            gridView
        }
    }

    private var gridView: some View {
        LazyVGrid(
            columns: Array(
                repeating: GridItem(
                    .flexible(),
                    spacing: LayoutInsets.smallInset,
                    alignment: .top
                ),
                count: Appearance.columnsCount
            ),
            alignment: .leading,
            spacing: LayoutInsets.smallInset
        ) {
            ForEach(Array(progressStatuses.enumerated()), id: \.offset) { _, progressStatus in
                ChallengeWidgetContentStateProgressGridItemView(
                    progressStatus: progressStatus
                )
            }
        }
    }
}

#if DEBUG
#Preview {
    ChallengeWidgetContentStateProgressGridView(
        progressStatuses: ChallengeWidgetViewStateContentHappeningNow.ProgressStatus.placeholder
    )
    .padding()
}
#endif
