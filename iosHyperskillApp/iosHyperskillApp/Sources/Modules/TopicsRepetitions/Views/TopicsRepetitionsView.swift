import SwiftUI

struct TopicsRepetitionsView: View {
    let topicsToRepeatCount: Int

    var body: some View {
        ScrollView {
            VStack(spacing: LayoutInsets.largeInset) {
                TopicsRepetitionsChartBlock(topicsToRepeatCount: topicsToRepeatCount)

                TopicsRepetitionsRepeatBlock(topicsToRepeatCount: topicsToRepeatCount)

                TopicsRepetitionsInfoBlock()
                    .padding(.bottom)
            }
        }
        .background(Color(ColorPalette.background))
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
