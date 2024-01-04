import SwiftUI

struct TopicsRepetitionsCountView: View {
    private let formatter = Formatter.default

    let topicsToRepeatCount: Int

    var body: some View {
        HomeWidgetCountView(
            countText: String(topicsToRepeatCount),
            description: formatter.topicsToRepeatTodayCount(topicsToRepeatCount)
        )
    }
}

#Preview {
    TopicsRepetitionsCountView(topicsToRepeatCount: 4)
}
