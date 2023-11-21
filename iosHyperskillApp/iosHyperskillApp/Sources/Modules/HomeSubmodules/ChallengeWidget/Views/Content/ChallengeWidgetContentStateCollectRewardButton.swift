import shared
import SwiftUI

struct ChallengeWidgetContentStateCollectRewardButton: View {
    let collectRewardButtonStateKs: ChallengeWidgetViewStateContentCollectRewardButtonStateKs

    let action: () -> Void

    var body: some View {
        switch collectRewardButtonStateKs {
        case .hidden:
            EmptyView()
        case .visible(let data):
            Button(
                data.title,
                action: action
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }
}

extension ChallengeWidgetContentStateCollectRewardButton {
    init(
        collectRewardButtonState: ChallengeWidgetViewStateContentCollectRewardButtonState,
        action: @escaping () -> Void
    ) {
        self.init(
            collectRewardButtonStateKs: ChallengeWidgetViewStateContentCollectRewardButtonStateKs(
                collectRewardButtonState
            ),
            action: action
        )
    }
}

#Preview {
    VStack {
        ChallengeWidgetContentStateCollectRewardButton(
            collectRewardButtonStateKs: .hidden,
            action: {}
        )

        ChallengeWidgetContentStateCollectRewardButton(
            collectRewardButtonStateKs: .visible(.init(title: "Collect Reward")),
            action: {}
        )
    }
    .padding()
}
