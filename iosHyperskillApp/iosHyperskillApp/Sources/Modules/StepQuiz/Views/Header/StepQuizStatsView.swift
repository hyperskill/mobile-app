import SwiftUI

extension StepQuizStatsView {
    struct Appearance {
        let primaryColor = Color.disabledText
    }
}

struct StepQuizStatsView: View {
    private(set) var appearance = Appearance()

    var text: String

    var body: some View {
        Text(text)
            .font(.subheadline)
            .foregroundColor(appearance.primaryColor)
    }
}

struct StepQuizStatsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizStatsView(text: "2438 users solved this problem. Latest completion was about 13 hours ago.")

            StepQuizStatsView(text: "2438 users solved this problem. Latest completion was about 13 hours ago.")
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
