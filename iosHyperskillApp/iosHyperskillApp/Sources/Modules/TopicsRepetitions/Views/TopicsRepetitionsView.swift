import SwiftUI

extension TopicsRepetitionsView {
    struct Appearance {
        let padding = LayoutInsets.largeInset
    }
}

struct TopicsRepetitionsView: View {
    private(set) var appearance = Appearance()

    let topicsToRepeatCount: Int

    var body: some View {
        ScrollView {
            VStack(spacing: appearance.padding) {
                TopicsRepetitionsChartBlock(topicsToRepeatCount: topicsToRepeatCount)
                    .padding(.top, appearance.padding)

                TopicsRepetitionsRepeatBlock(topicsToRepeatCount: topicsToRepeatCount)

                TopicsRepetitionsInfoBlock()
                    .padding(.bottom, appearance.padding)
            }
        }
        .background(Color.systemGroupedBackground)
        .navigationBarHidden(false)
        .navigationTitle(Strings.TopicsRepetitions.Card.titleUncompleted)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct TopicsRepetitionsView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsView(topicsToRepeatCount: 4)
    }
}
