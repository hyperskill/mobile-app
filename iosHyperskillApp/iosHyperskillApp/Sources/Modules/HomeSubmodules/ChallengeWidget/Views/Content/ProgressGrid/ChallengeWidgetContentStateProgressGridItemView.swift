import shared
import SwiftUI

struct ChallengeWidgetContentStateProgressGridItemView: View {
    let progressStatus: ChallengeWidgetViewStateContentHappeningNow.ProgressStatus

    var body: some View {
        ZStack(alignment: .center) {
            if progressStatus == .missed {
                Image(systemName: "xmark")
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(widthHeight: 10)
                    .foregroundColor(.secondaryText)
            }
        }
        .frame(minWidth: 32, minHeight: 16)
        .background(progressStatus == .completed ? Color(ColorPalette.primary) : Color(ColorPalette.onSurfaceAlpha9))
        .addBorder(
            color: progressStatus == .active ? Color(ColorPalette.primary) : .clear,
            width: progressStatus == .active ? 1 : 0,
            cornerRadius: 4
        )
    }
}

#Preview {
    VStack {
        ChallengeWidgetContentStateProgressGridItemView(progressStatus: .inactive)
        ChallengeWidgetContentStateProgressGridItemView(progressStatus: .active)
        ChallengeWidgetContentStateProgressGridItemView(progressStatus: .completed)
        ChallengeWidgetContentStateProgressGridItemView(progressStatus: .missed)
    }
    .padding()
}
