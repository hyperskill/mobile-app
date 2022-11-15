import SwiftUI

struct TopicsRepetitionsView: View {
    let topicsToRepeatCount: Int
    var body: some View {
        ScrollView {
            BackgroundView()

            VStack(spacing: LayoutInsets.largeInset) {
                TopicsRepetitionsStatisticView(topicsToRepeatCount: topicsToRepeatCount)

                TopicsToRepeatView(topicsToRepeatCount: topicsToRepeatCount)

                RepetitionsExplanationView()
            }
        }
        .padding()
        .background(Color(ColorPalette.background))
        .navigationBarHidden(false)
        .navigationTitle(Strings.TopicsRepetitions.cardTitleUncompleted)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct TopicsRepetitionsView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsView(topicsToRepeatCount: 4)
    }
}
